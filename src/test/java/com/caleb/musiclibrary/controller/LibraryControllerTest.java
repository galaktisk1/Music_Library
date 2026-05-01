package com.caleb.musiclibrary.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.caleb.musiclibrary.index.LibraryIndex;
import com.caleb.musiclibrary.index.SearchService;
import com.caleb.musiclibrary.model.Album;
import com.caleb.musiclibrary.model.Track;
import com.caleb.musiclibrary.scan.LibraryScanner;

class LibraryControllerTest {

    @Test
    void searchAlbumsByArtistMatchesIgnoringCase() {
        FakeLibraryScanner scanner = new FakeLibraryScanner();
        Path root = Paths.get("C:\\music");
        scanner.albumsToReturn = List.of(
            album("Hollow Crown", "Architects", 2009, root.resolve("Hollow Crown")),
            album("Discovery", "Daft Punk", 2001, root.resolve("Discovery"))
        );

        LibraryController controller = new LibraryController(scanner, new SearchService(new LibraryIndex()));
        controller.scanFolder(root);

        List<Album> results = controller.searchAlbumsByArtist("architect");
        System.out.println("LibraryControllerTest artist album match: " + results.get(0).getTitle());

        assertEquals(1, results.size());
        assertEquals("Hollow Crown", results.get(0).getTitle());
    }

    @Test
    void searchAlbumsByTrackReturnsMatchingAlbum() {
        FakeLibraryScanner scanner = new FakeLibraryScanner();
        Path architectsPath = Paths.get("C:\\music\\Architects\\Hollow Crown");
        Path daftPunkPath = Paths.get("C:\\music\\Daft Punk\\Discovery");

        scanner.albumsToReturn = List.of(
            album("Hollow Crown", "Architects", 2009, architectsPath),
            album("Discovery", "Daft Punk", 2001, daftPunkPath)
        );
        scanner.trackMap.put(architectsPath, List.of(track("Early Grave", 1)));
        scanner.trackMap.put(daftPunkPath, List.of(track("Digital Love", 11)));

        LibraryController controller = new LibraryController(scanner, new SearchService(new LibraryIndex()));
        controller.scanFolder(Paths.get("C:\\music"));

        List<Album> results = controller.searchAlbumsByTrack("digital");
        System.out.println("LibraryControllerTest track search album match: " + results.get(0).getTitle());

        assertEquals(1, results.size());
        assertEquals("Discovery", results.get(0).getTitle());
    }

    @Test
    void getTracksForAlbumReturnsScannerTracks() {
        FakeLibraryScanner scanner = new FakeLibraryScanner();
        Path albumPath = Paths.get("C:\\music\\Architects\\Hollow Crown");
        Album album = album("Hollow Crown", "Architects", 2009, albumPath);
        scanner.trackMap.put(albumPath, List.of(track("Early Grave", 1)));

        LibraryController controller = new LibraryController(scanner, new SearchService(new LibraryIndex()));

        List<Track> results = controller.getTracksForAlbum(album);
        System.out.println("LibraryControllerTest track count for album: " + results.size());

        assertEquals(1, results.size());
        assertEquals("Early Grave", results.get(0).getTitle());
    }

    private Album album(String title, String artist, Integer year, Path folderPath) {
        Album album = new Album();
        album.setTitle(title);
        album.setArtist(artist);
        album.setYear(year);
        album.setFolderPath(folderPath);
        return album;
    }

    private Track track(String title, Integer trackNumber) {
        Track track = new Track();
        track.setTitle(title);
        track.setTrackNumber(trackNumber);
        return track;
    }

    private static class FakeLibraryScanner extends LibraryScanner {
        private List<Album> albumsToReturn = Collections.emptyList();
        private final Map<Path, List<Track>> trackMap = new HashMap<>();

        @Override
        public List<Album> scanAlbums(Path rootFolder) {
            return albumsToReturn;
        }

        @Override
        public List<Track> scanAlbumTracks(Path albumFolder) {
            return trackMap.getOrDefault(albumFolder, Collections.emptyList());
        }
    }
}
