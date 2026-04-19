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
        List<LibraryRecord> records = scanner.scan(folder);
        searchService.addRecords(records);
        return records;
    }

    public List<LibraryRecord> searchByTitle(String title) {
        return searchService.searchByTitle(title);
    }

    public List<LibraryRecord> searchByArtist(String artist) {
        return searchService.searchByArtist(artist);
    }

    public List<LibraryRecord> searchByAlbum(String album) {
        return searchService.searchByAlbum(album);
    }

    public void saveLibrary(List<LibraryRecord> records) {
        if (repository != null) {
            repository.save(records);
        }
    }

    public List<LibraryRecord> loadLibrary() {
        if (repository == null) {
            return Collections.emptyList();
        }

        List<LibraryRecord> records = repository.load();
        searchService.addRecords(records);
        return records;
    }
}
