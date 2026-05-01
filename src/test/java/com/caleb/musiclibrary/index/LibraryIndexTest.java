package com.caleb.musiclibrary.index;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.caleb.musiclibrary.model.Track;

class LibraryIndexTest {

    @Test
    void findByTitleMatchesPartialTextIgnoringCase() {
        LibraryIndex index = new LibraryIndex();
        index.addRecords(List.of(
            record("Early Grave", "Architects", "Hollow Crown"),
            record("Gravedigger", "Architects", "Lost Forever // Lost Together"),
            record("Digital Love", "Daft Punk", "Discovery")
        ));

        List<Track> results = index.findByTitle("grave");
        System.out.println("LibraryIndexTest title matches: " + results.size());

        assertEquals(2, results.size());
        assertEquals("Early Grave", results.get(0).getTitle());
        assertEquals("Gravedigger", results.get(1).getTitle());
    }

    @Test
    void findByArtistReturnsEmptyListForBlankSearch() {
        LibraryIndex index = new LibraryIndex();
        index.addRecord(record("Early Grave", "Architects", "Hollow Crown"));

        List<Track> results = index.findByArtist("   ");
        System.out.println("LibraryIndexTest blank artist search returned: " + results.size());

        assertTrue(results.isEmpty());
    }

    @Test
    void clearRemovesAllStoredRecords() {
        LibraryIndex index = new LibraryIndex();
        index.addRecords(List.of(
            record("Early Grave", "Architects", "Hollow Crown"),
            record("Digital Love", "Daft Punk", "Discovery")
        ));

        index.clear();
        System.out.println("LibraryIndexTest records after clear: " + index.getAllRecords().size());

        assertTrue(index.getAllRecords().isEmpty());
    }

    private Track record(String title, String artist, String album) {
        Track track = new Track();
        track.setTitle(title);
        track.setArtist(artist);
        track.setAlbum(album);
        return track;
    }
}
