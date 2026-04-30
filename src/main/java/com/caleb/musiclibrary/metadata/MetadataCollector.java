package com.caleb.musiclibrary.metadata;

import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;

import com.caleb.musiclibrary.model.Album;
import com.caleb.musiclibrary.model.LibraryRecord;

/**
 * Extracts audio file tags and converts them into normalized library records.
 */
public class MetadataCollector {
    private static final Pattern FIRST_NUMBER = Pattern.compile("\\d+");
    private static final Pattern YEAR_PREFIX = Pattern.compile("^\\d{4}\\s+(.+)$");
    private static final Pattern DISC_LABEL = Pattern.compile("(?i)^disc\\s+(\\d+)(?:\\s+(.*))?$");

    public LibraryRecord collect(Path file) throws Exception {
        // I looked up how to use jaudiotagger to read MP3 tag data.
        AudioFile audio = AudioFileIO.read(file.toFile());
        Tag tag = audio.getTag();

        LibraryRecord record = new LibraryRecord();
        record.setFilePath(file.toString());
        record.setTitle(read(tag, FieldKey.TITLE, stripExtension(file.getFileName().toString())));
        record.setArtist(read(tag, FieldKey.ARTIST, "Unknown Artist"));
        record.setAlbum(read(tag, FieldKey.ALBUM, "Unknown Album"));
        record.setYear(parseInt(read(tag, FieldKey.YEAR, "")));
        record.setTrackNumber(parseInt(read(tag, FieldKey.TRACK, "")));
        return record;
    }

    public Album collectAlbum(Path albumFolder, List<Path> albumFiles) throws Exception {
        Path sampleFile = albumFiles.get(0);
        AudioFile audio = AudioFileIO.read(sampleFile.toFile());
        Tag tag = audio.getTag();
        String albumTitle = read(tag, FieldKey.ALBUM, albumFolder.getFileName().toString());

        Album album = new Album();
        album.setFolderPath(albumFolder);
        album.setTitle(buildAlbumDisplayTitle(albumTitle, albumFolder));
        album.setArtist(read(tag, FieldKey.ARTIST, "Unknown Artist"));
        album.setYear(parseInt(read(tag, FieldKey.YEAR, "")));
        album.setCoverArt(readCoverArt(tag));
        return album;
    }

    private String read(Tag tag, FieldKey key, String fallback) {
        if (tag == null) {
            return fallback;
        }
        String value = tag.getFirst(key);
        return (value == null || value.isBlank()) ? fallback : value.trim();
    }

    private Integer parseInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        Matcher matcher = FIRST_NUMBER.matcher(value);
        if (!matcher.find()) {
            return null;
        }

        return Integer.parseInt(matcher.group());
    }

    private byte[] readCoverArt(Tag tag) {
        if (tag == null) {
            return null;
        }

        // I looked up how jaudiotagger stores embedded album artwork.
        Artwork artwork = tag.getFirstArtwork();
        if (artwork == null) {
            return null;
        }

        return artwork.getBinaryData();
    }

    private String buildAlbumDisplayTitle(String albumTitle, Path albumFolder) {
        String folderName = fileName(albumFolder);

        // This part handles odd folder layouts like Disc 1 / Disc 2 and soundtrack collections.
        if (isDiscLabel(folderName)) {
            String rootAlbumTitle = findRootAlbumTitle(albumFolder);
            String baseTitle = albumTitle;
            if (!rootAlbumTitle.isBlank()) {
                baseTitle = rootAlbumTitle;
            }

            String collectionName = findCollectionName(albumFolder, rootAlbumTitle);
            String discSuffix = buildDiscSuffix(folderName, baseTitle);

            String displayTitle = baseTitle;
            if (hasUsefulCollectionName(collectionName, baseTitle)) {
                displayTitle += " - " + collectionName;
            }
            displayTitle += " - " + discSuffix;

            return displayTitle;
        }

        return albumTitle;
    }

    private boolean isDiscLabel(String name) {
        if (name == null) {
            return false;
        }

        return DISC_LABEL.matcher(name.trim()).matches();
    }

    private String buildDiscSuffix(String folderName, String baseTitle) {
        Matcher matcher = DISC_LABEL.matcher(folderName);
        if (!matcher.matches()) {
            return folderName;
        }

        String discNumber = matcher.group(1);
        String extraText = matcher.group(2) == null ? "" : matcher.group(2).trim();
        String discText = "Disc " + discNumber;

        if (extraText.isBlank()) {
            return discText;
        }

        if (containsIgnoreCase(extraText, baseTitle)) {
            return discText;
        }

        return discText + " - " + extraText;
    }

    private String findRootAlbumTitle(Path albumFolder) {
        Path current = albumFolder;

        while (current != null) {
            String cleaned = stripYearPrefix(fileName(current));
            String original = fileName(current);

            if (!cleaned.equals(original) && !cleaned.isBlank()) {
                return cleaned;
            }

            current = current.getParent();
        }

        return "";
    }

    private String findCollectionName(Path albumFolder, String rootAlbumTitle) {
        Path parent = albumFolder.getParent();
        if (parent == null) {
            return "";
        }

        String collectionName = stripYearPrefix(fileName(parent));
        if (collectionName.isBlank()) {
            return "";
        }

        if (!rootAlbumTitle.isBlank() && collectionName.equalsIgnoreCase(rootAlbumTitle)) {
            return "";
        }

        return collectionName;
    }

    private boolean hasUsefulCollectionName(String collectionName, String albumTitle) {
        if (collectionName == null || collectionName.isBlank()) {
            return false;
        }

        if (collectionName.equalsIgnoreCase("covers")) {
            return false;
        }

        return !containsIgnoreCase(albumTitle, collectionName);
    }

    private String stripYearPrefix(String name) {
        if (name == null) {
            return "";
        }

        Matcher matcher = YEAR_PREFIX.matcher(name.trim());
        if (matcher.matches()) {
            return matcher.group(1).trim();
        }

        return name.trim();
    }

    private boolean containsIgnoreCase(String left, String right) {
        if (left == null || right == null) {
            return false;
        }

        return left.toLowerCase().contains(right.toLowerCase());
    }

    private String fileName(Path path) {
        if (path == null || path.getFileName() == null) {
            return "";
        }

        return path.getFileName().toString().trim();
    }

    private String stripExtension(String name) {
        int dot = name.lastIndexOf('.');
        return dot > 0 ? name.substring(0, dot) : name;
    }
}
