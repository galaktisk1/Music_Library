package com.caleb.musiclibrary.scan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.caleb.musiclibrary.metadata.MetadataCollector;
import com.caleb.musiclibrary.model.Album;
import com.caleb.musiclibrary.model.LibraryRecord;

class LibraryScannerTest {

    @Test
    void scanAlbumsSortsByYearThenArtistThenTitle() {
        Path root = Paths.get("C:\\music");
        Path albumA = root.resolve("AlbumA");
        Path albumB = root.resolve("AlbumB");

        FakeFileWalker walker = new FakeFileWalker();
        walker.albumFolders = List.of(albumA, albumB);
        walker.audioFilesByFolder.put(albumA, List.of(albumA.resolve("01.mp3")));
        walker.audioFilesByFolder.put(albumB, List.of(albumB.resolve("01.mp3")));

        FakeMetadataCollector collector = new FakeMetadataCollector();
        collector.albumMap.put(albumA, album("Hollow Crown", "Architects", 2009, albumA));
        collector.albumMap.put(albumB, album("Korn", "Korn", 1994, albumB));

        LibraryScanner scanner = new LibraryScanner(walker, collector);

        List<Album> results = scanner.scanAlbums(root);
        System.out.println("LibraryScannerTest first sorted album: " + results.get(0).getTitle());

        assertEquals(2, results.size());
        assertEquals("Korn", results.get(0).getTitle());
        assertEquals("Hollow Crown", results.get(1).getTitle());
    }

    @Test
    void scanAlbumTracksSortsByTrackNumberThenTitle() {
        Path albumFolder = Paths.get("C:\\music\\Architects\\Hollow Crown");
        Path firstTrack = albumFolder.resolve("01.mp3");
        Path secondTrack = albumFolder.resolve("02.mp3");
        Path thirdTrack = albumFolder.resolve("03.mp3");

        FakeFileWalker walker = new FakeFileWalker();
        walker.audioFilesByFolder.put(albumFolder, List.of(firstTrack, secondTrack, thirdTrack));

        FakeMetadataCollector collector = new FakeMetadataCollector();
        collector.trackMap.put(firstTrack, track("Numbers Count for Nothing", 3));
        collector.trackMap.put(secondTrack, track("Early Grave", 1));
        collector.trackMap.put(thirdTrack, track("Dethroned", 2));

        LibraryScanner scanner = new LibraryScanner(walker, collector);

        List<LibraryRecord> results = scanner.scanAlbumTracks(albumFolder);
        System.out.println("LibraryScannerTest first sorted track: " + results.get(0).getTitle());

        assertEquals(3, results.size());
        assertEquals("Early Grave", results.get(0).getTitle());
        assertEquals("Dethroned", results.get(1).getTitle());
        assertEquals("Numbers Count for Nothing", results.get(2).getTitle());
    }

    private Album album(String title, String artist, Integer year, Path folderPath) {
        Album album = new Album();
        album.setTitle(title);
        album.setArtist(artist);
        album.setYear(year);
        album.setFolderPath(folderPath);
        return album;
    }

    private LibraryRecord track(String title, Integer trackNumber) {
        LibraryRecord record = new LibraryRecord();
        record.setTitle(title);
        record.setTrackNumber(trackNumber);
        return record;
    }

    private static class FakeFileWalker extends FileWalker {
        private List<Path> albumFolders = List.of();
        private final Map<Path, List<Path>> audioFilesByFolder = new HashMap<>();

        @Override
        public List<Path> findAlbumFolders(Path root) {
            return albumFolders;
        }

        @Override
        public List<Path> findAudioFiles(Path root) {
            return audioFilesByFolder.getOrDefault(root, List.of());
        }
    }

    private static class FakeMetadataCollector extends MetadataCollector {
        private final Map<Path, Album> albumMap = new HashMap<>();
        private final Map<Path, LibraryRecord> trackMap = new HashMap<>();

        @Override
        public Album collectAlbum(Path albumFolder, List<Path> albumFiles) {
            return albumMap.get(albumFolder);
        }

        @Override
        public LibraryRecord collect(Path file) {
            return trackMap.get(file);
        }
    }
}
