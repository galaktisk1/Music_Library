package com.caleb.musiclibrary.metadata;

import java.nio.file.Path;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.caleb.musiclibrary.model.LibraryRecord;

/**
 * Extracts audio file tags and converts them into normalized library records.
 */
public class MetadataCollector {
    public LibraryRecord collect(Path file) throws Exception {
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

    private String read(Tag tag, FieldKey key, String fallback) {
        if (tag == null) {
            return fallback;
        }
        String value = tag.getFirst(key);
        return (value == null || value.isBlank()) ? fallback : value.trim();
    }

    private Integer parseInt(String value) {
        if (value == null || value.isBlank() || !value.matches("\\d+")) {
            return null;
        }
        return Integer.parseInt(value);
    }

    private String stripExtension(String name) {
        int dot = name.lastIndexOf('.');
        return dot > 0 ? name.substring(0, dot) : name;
    }
}
