package com.caleb.musiclibrary.scan;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.caleb.musiclibrary.metadata.MetadataCollector;
import com.caleb.musiclibrary.model.Album;
import com.caleb.musiclibrary.model.LibraryRecord;

/**
 * Runs library scanning by combining file discovery and metadata collection.
 */
public class LibraryScanner {
    private final FileWalker fileWalker;
    private final MetadataCollector metadataCollector;

    public LibraryScanner() {
        this(new FileWalker(), new MetadataCollector());
    }

    public LibraryScanner(FileWalker fileWalker, MetadataCollector metadataCollector) {
        this.fileWalker = fileWalker;
        this.metadataCollector = metadataCollector;
    }

    public List<Album> scanAlbums(Path rootFolder) {
        // I changed this to scan albums first so the app loads faster, then tracks are loaded later.
        List<Album> albums = new ArrayList<>();
        List<Path> albumFolders = fileWalker.findAlbumFolders(rootFolder);

        for (Path albumFolder : albumFolders) {
            List<Path> albumFiles = fileWalker.findAudioFiles(albumFolder);
            if (albumFiles.isEmpty()) {
                continue;
            }

            try {
                Album album = metadataCollector.collectAlbum(albumFolder, albumFiles);
                albums.add(album);
            } catch (Exception e) {
                // skip albums that fail metadata reading for now
            }
        }

        albums.sort(Comparator
            .comparing((Album album) -> album.getYear() == null ? Integer.MAX_VALUE : album.getYear())
            .thenComparing(album -> lower(album.getArtist()))
            .thenComparing(album -> lower(album.getTitle())));

        return albums;
    }

    public List<LibraryRecord> scanAlbumTracks(Path albumFolder) {
        List<LibraryRecord> records = new ArrayList<>();
        List<Path> audioFiles = fileWalker.findAudioFiles(albumFolder);

        for (Path audioFile : audioFiles) {
            try {
                LibraryRecord record = metadataCollector.collect(audioFile);
                records.add(record);
            } catch (Exception e) {
                // skip files that fail metadata reading for now
            }
        }

        records.sort(Comparator
            .comparing((LibraryRecord record) -> record.getTrackNumber() == null ? Integer.MAX_VALUE : record.getTrackNumber())
            .thenComparing(record -> lower(record.getTitle())));

        return records;
    }

    private String lower(String value) {
        if (value == null) {
            return "";
        }

        return value.toLowerCase();
    }
}
