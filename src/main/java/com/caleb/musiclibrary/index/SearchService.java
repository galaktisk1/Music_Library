package com.caleb.musiclibrary.index;

import java.util.Collections;
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
        // TODO: Add scanned records to the searchable library index.
    }

    public List<LibraryRecord> searchByTitle(String title) {
        // TODO: Return matching track records. The UI should group these by
        // album and highlight the searched-for track inside the album view.
        return Collections.emptyList();
    }

    public List<LibraryRecord> searchByArtist(String artist) {
        // TODO: Return matching artist records.
        return Collections.emptyList();
    }

    public List<LibraryRecord> searchByAlbum(String album) {
        // TODO: Return matching album records.
        return Collections.emptyList();
    }
}
