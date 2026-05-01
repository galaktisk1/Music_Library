package com.caleb.musiclibrary.index;

import java.util.Collections;
import java.util.List;

import com.caleb.musiclibrary.model.Track;

/**
 * Provides user-facing search operations over the indexed music library.
 */
public class SearchService {
    private final LibraryIndex index;

    public SearchService(LibraryIndex index) {
        this.index = index;
    }

    public void addRecords(List<Track> records) {
        index.addRecords(records);
    }

    public void replaceRecords(List<Track> records) {
        index.clear();
        index.addRecords(records);
    }

    public List<Track> searchByTitle(String title) {
        return index.findByTitle(title);
    }

    public List<Track> searchByArtist(String artist) {
        return index.findByArtist(artist);
    }

    public List<Track> searchByAlbum(String album) {
        return index.findByAlbum(album);
    }

    public List<Track> getAllRecords() {
        List<Track> records = index.getAllRecords();
        if (records == null) {
            return Collections.emptyList();
        }

        return records;
    }
}
