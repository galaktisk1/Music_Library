package com.caleb.musiclibrary.index;

import java.util.List;

import com.caleb.musiclibrary.model.LibraryRecord;

/**
 * Provides user-facing search operations over the indexed music library.
 */
public class SearchService {
    private final LibraryIndex index;

    public SearchService(LibraryIndex index) {
        this.index = index;
    }

    public void addRecords(List<LibraryRecord> records) {
        index.addRecords(records);
    }

    public List<LibraryRecord> searchByTitle(String title) {
        // Returns matching track records. The UI can group these by album and
        // highlight the searched-for track inside the matching album view.
        return index.findByTitle(title);
    }

    public List<LibraryRecord> searchByArtist(String artist) {
        return index.findByArtist(artist);
    }

    public List<LibraryRecord> searchByAlbum(String album) {
        return index.findByAlbum(album);
    }
}
