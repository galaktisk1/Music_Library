package com.caleb.musiclibrary.storage;

import java.util.List;

import com.caleb.musiclibrary.model.LibraryRecord;

/**
 * Defines the persistence contract for saving and loading library data.
 */
public interface LibraryRepository {
    void save(List<LibraryRecord> records);

    List<LibraryRecord> load();
}
