package com.caleb.musiclibrary.index;

import java.util.ArrayList;
import java.util.List;

import com.caleb.musiclibrary.model.LibraryRecord;

/**
 * Stores library records in memory and supports simple search methods.
 */
public class LibraryIndex {
    private final List<LibraryRecord> records = new ArrayList<>();

    public void addRecord(LibraryRecord record) {
        if (record != null) {
            records.add(record);
        }
    }

    public void addRecords(List<LibraryRecord> records) {
        if (records == null) {
            return;
        }

        this.records.addAll(records);
    }

    public List<LibraryRecord> getAllRecords() {
        return records;
    }

    public void clear() {
        records.clear();
    }

    public List<LibraryRecord> findByTitle(String title) {
        List<LibraryRecord> matches = new ArrayList<>();
        if (title == null || title.isBlank()) {
            return matches;
        }

        String searchText = title.toLowerCase();
        for (LibraryRecord record : records) {
            String recordTitle = record.getTitle();
            if (recordTitle != null && recordTitle.toLowerCase().contains(searchText)) {
                matches.add(record);
            }
        }

        return matches;
    }

    public List<LibraryRecord> findByArtist(String artist) {
        List<LibraryRecord> matches = new ArrayList<>();
        if (artist == null || artist.isBlank()) {
            return matches;
        }

        String searchText = artist.toLowerCase();
        for (LibraryRecord record : records) {
            String recordArtist = record.getArtist();
            if (recordArtist != null && recordArtist.toLowerCase().contains(searchText)) {
                matches.add(record);
            }
        }

        return matches;
    }

    public List<LibraryRecord> findByAlbum(String album) {
        List<LibraryRecord> matches = new ArrayList<>();
        if (album == null || album.isBlank()) {
            return matches;
        }

        String searchText = album.toLowerCase();
        for (LibraryRecord record : records) {
            String recordAlbum = record.getAlbum();
            if (recordAlbum != null && recordAlbum.toLowerCase().contains(searchText)) {
                matches.add(record);
            }
        }

        return matches;
    }
}
