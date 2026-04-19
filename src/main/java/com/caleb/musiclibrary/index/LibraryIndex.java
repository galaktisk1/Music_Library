package com.caleb.musiclibrary.index;

import java.util.Collections;
import java.util.List;

import com.caleb.musiclibrary.model.LibraryRecord;

/**
 * Maintains in-memory structures for fast lookup of library records.
 */
public class LibraryIndex {
    public void addRecord(LibraryRecord record) {
        // TODO: Store one library record in the index.
    }

    public void addRecords(List<LibraryRecord> records) {
        // TODO: Store multiple library records in the index.
    }

    public List<LibraryRecord> getAllRecords() {
        // TODO: Return all indexed records.
        return Collections.emptyList();
    }

    public List<LibraryRecord> findByTitle(String title) {
        // TODO: Search indexed records by track title.
        return Collections.emptyList();
    }

    public List<LibraryRecord> findByArtist(String artist) {
        // TODO: Search indexed records by artist.
        return Collections.emptyList();
    }

    public List<LibraryRecord> findByAlbum(String album) {
        // TODO: Search indexed records by album.
        return Collections.emptyList();
    }
}
