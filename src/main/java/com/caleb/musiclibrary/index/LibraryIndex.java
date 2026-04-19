package com.caleb.musiclibrary.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.caleb.musiclibrary.model.LibraryRecord;

/**
 * Maintains in-memory structures for fast lookup of library records.
 */
public class LibraryIndex {
    private final List<LibraryRecord> records = new ArrayList<>();

    public void addRecord(LibraryRecord record) {
        records.add(record);
    }

    public void addRecords(List<LibraryRecord> records) {
        this.records.addAll(records);
    }

    public List<LibraryRecord> getAllRecords() {
        return Collections.unmodifiableList(records);
    }

    public List<LibraryRecord> findByTitle(String title) {
        return findByField(title, Field.TITLE);
    }

    public List<LibraryRecord> findByArtist(String artist) {
        return findByField(artist, Field.ARTIST);
    }

    public List<LibraryRecord> findByAlbum(String album) {
        return findByField(album, Field.ALBUM);
    }

    private List<LibraryRecord> findByField(String query, Field field) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        String normalizedQuery = query.toLowerCase();
        List<LibraryRecord> matches = new ArrayList<>();

        for (LibraryRecord record : records) {
            String value = valueFor(record, field);
            if (value != null && value.toLowerCase().contains(normalizedQuery)) {
                matches.add(record);
            }
        }

        return matches;
    }

    private String valueFor(LibraryRecord record, Field field) {
        return switch (field) {
            case TITLE -> record.getTitle();
            case ARTIST -> record.getArtist();
            case ALBUM -> record.getAlbum();
        };
    }

    private enum Field {
        TITLE,
        ARTIST,
        ALBUM
    }
}
