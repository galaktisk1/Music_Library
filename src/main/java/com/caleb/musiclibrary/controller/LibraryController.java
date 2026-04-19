package com.caleb.musiclibrary.controller;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import com.caleb.musiclibrary.index.SearchService;
import com.caleb.musiclibrary.model.LibraryRecord;
import com.caleb.musiclibrary.scan.LibraryScanner;
import com.caleb.musiclibrary.storage.LibraryRepository;

/**
 * Coordinates user actions between the UI and the library services.
 */
public class LibraryController {
    private final LibraryScanner scanner;
    private final SearchService searchService;
    private final LibraryRepository repository;

    public LibraryController(LibraryScanner scanner, SearchService searchService) {
        this(scanner, searchService, null);
    }

    public LibraryController(
        LibraryScanner scanner,
        SearchService searchService,
        LibraryRepository repository
    ) {
        this.scanner = scanner;
        this.searchService = searchService;
        this.repository = repository;
    }

    public List<LibraryRecord> scanFolder(Path folder) {
        // TODO: Scan the folder, update the search index, and return records
        // for the UI results screen.
        return Collections.emptyList();
    }

    public List<LibraryRecord> searchByTitle(String title) {
        // TODO: Track title searches should lead the UI to the containing
        // album, where the matching track can be highlighted in the track list.
        return Collections.emptyList();
    }

    public List<LibraryRecord> searchByArtist(String artist) {
        // TODO: Search for records by artist.
        return Collections.emptyList();
    }

    public List<LibraryRecord> searchByAlbum(String album) {
        // TODO: Search for records by album.
        return Collections.emptyList();
    }

    public void saveLibrary(List<LibraryRecord> records) {
        // TODO: Save library records through the repository.
    }

    public List<LibraryRecord> loadLibrary() {
        // TODO: Load library records through the repository.
        return Collections.emptyList();
    }
}
