package com.caleb.musiclibrary.controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.caleb.musiclibrary.index.SearchService;
import com.caleb.musiclibrary.model.Album;
import com.caleb.musiclibrary.model.LibraryRecord;
import com.caleb.musiclibrary.scan.LibraryScanner;

/**
 * Coordinates user actions between the UI and the library services.
 */
public class LibraryController {
    private final LibraryScanner scanner;
    private final SearchService searchService;
    private final List<Album> albums = new ArrayList<>();

    public LibraryController(LibraryScanner scanner, SearchService searchService) {
        this.scanner = scanner;
        this.searchService = searchService;
    }

    public List<Album> scanFolder(Path folder) {
        List<Album> scannedAlbums = scanner.scanAlbums(folder);
        albums.clear();
        albums.addAll(scannedAlbums);
        return scannedAlbums;
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

    public void addRecords(List<LibraryRecord> records) {
        searchService.addRecords(records);
    }

    public List<LibraryRecord> getAllRecords() {
        return searchService.getAllRecords();
    }

    public List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }

    public List<Album> searchAlbumsByArtist(String artist) {
        List<Album> matches = new ArrayList<>();
        if (artist == null || artist.isBlank()) {
            return matches;
        }

        String searchText = artist.toLowerCase();
        for (Album album : albums) {
            String albumArtist = album.getArtist();
            if (albumArtist != null && albumArtist.toLowerCase().contains(searchText)) {
                matches.add(album);
            }
        }

        return matches;
    }

    public List<Album> searchAlbumsByTitle(String title) {
        List<Album> matches = new ArrayList<>();
        if (title == null || title.isBlank()) {
            return matches;
        }

        String searchText = title.toLowerCase();
        for (Album album : albums) {
            String albumTitle = album.getTitle();
            if (albumTitle != null && albumTitle.toLowerCase().contains(searchText)) {
                matches.add(album);
            }
        }

        return matches;
    }

    public List<Album> searchAlbumsByTrack(String trackTitle) {
        List<Album> matches = new ArrayList<>();
        if (trackTitle == null || trackTitle.isBlank()) {
            return matches;
        }

        String searchText = trackTitle.toLowerCase();
        for (Album album : albums) {
            List<LibraryRecord> tracks = scanner.scanAlbumTracks(album.getFolderPath());
            for (LibraryRecord track : tracks) {
                String title = track.getTitle();
                if (title != null && title.toLowerCase().contains(searchText)) {
                    matches.add(album);
                    break;
                }
            }
        }

        return matches;
    }

    public List<LibraryRecord> getTracksForAlbum(Album album) {
        if (album == null || album.getFolderPath() == null) {
            return Collections.emptyList();
        }

        return scanner.scanAlbumTracks(album.getFolderPath());
    }
}
