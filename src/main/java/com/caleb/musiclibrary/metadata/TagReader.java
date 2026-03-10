package com.caleb.musiclibrary.metadata;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class TagReader {

    public Map<String, String> readTags(Path file) throws Exception {
        Map<String, String> tags = new HashMap<>();
        tags.put("filePath", file.toString());

        AudioFile audio = AudioFileIO.read(file.toFile());
        Tag tag = audio.getTag();

        tags.put("title", read(tag, FieldKey.TITLE, stripExtension(file.getFileName().toString())));
        tags.put("artist", read(tag, FieldKey.ARTIST, "Unknown Artist"));
        tags.put("album", read(tag, FieldKey.ALBUM, "Unknown Album"));
        tags.put("year", read(tag, FieldKey.YEAR, ""));
        tags.put("trackNumber", read(tag, FieldKey.TRACK, ""));

        return tags;
    }


    // helper to safely read a tag field with a fallback if the tag or field is missing/empty
    private String read(Tag tag, FieldKey key, String fallback) {
        if (tag == null) return fallback;
        String value = tag.getFirst(key);
        return (value == null || value.isBlank()) ? fallback : value.trim();
    }

    // this is a strip extension fallback for title when no tags are present, so we can at least have a name to work with
    private String stripExtension(String name) {
        int dot = name.lastIndexOf('.');
        return dot > 0 ? name.substring(0, dot) : name;
    }
}
