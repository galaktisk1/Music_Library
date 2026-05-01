package com.caleb.musiclibrary.index;

import java.util.ArrayList;
import java.util.List;

import com.caleb.musiclibrary.model.Track;

/**
 * Stores library records in memory and supports simple search methods.
 */
public class LibraryIndex {
    private final List<Track> records = new ArrayList<>();

    public void addRecord(Track record) {
        if (record != null) {
            records.add(record);
        }
    }

    public void addRecords(List<Track> records) {
        if (records == null) {
            return;
        }

        this.records.addAll(records);
    }

    public List<Track> getAllRecords() {
        return records;
    }

    public void clear() {
        records.clear();
    }

    public List<Track> findByTitle(String title) {
        List<Track> matches = new ArrayList<>();
        if (title == null || title.isBlank()) {
            return matches;
        }

        String searchText = title.toLowerCase();
        for (Track record : records) {
            String recordTitle = record.getTitle();
            if (recordTitle != null && recordTitle.toLowerCase().contains(searchText)) {
                matches.add(record);
            }
        }

        return matches;
    }

    public List<Track> findByArtist(String artist) {
        List<Track> matches = new ArrayList<>();
        if (artist == null || artist.isBlank()) {
            return matches;
        }

        String searchText = artist.toLowerCase();
        for (Track record : records) {
            String recordArtist = record.getArtist();
            if (recordArtist != null && recordArtist.toLowerCase().contains(searchText)) {
                matches.add(record);
            }
        }

        return matches;
    }

    public List<Track> findByAlbum(String album) {
        List<Track> matches = new ArrayList<>();
        if (album == null || album.isBlank()) {
            return matches;
        }

        String searchText = album.toLowerCase();
        for (Track record : records) {
            String recordAlbum = record.getAlbum();
            if (recordAlbum != null && recordAlbum.toLowerCase().contains(searchText)) {
                matches.add(record);
            }
        }

        return matches;
    }
}
