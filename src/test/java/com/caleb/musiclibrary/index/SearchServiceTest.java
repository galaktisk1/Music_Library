package com.caleb.musiclibrary.index;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.caleb.musiclibrary.model.LibraryRecord;

class SearchServiceTest {
    @Test
    void searchByArtistReturnsMatchingRecords() {
        LibraryIndex index = new LibraryIndex();
        SearchService searchService = new SearchService(index);

        LibraryRecord first = new LibraryRecord();
        first.setTitle("Early Grave");
        first.setArtist("Architects");
        first.setAlbum("Hollow Crown");

        LibraryRecord second = new LibraryRecord();
        second.setTitle("Dethroned");
        second.setArtist("Architects");
        second.setAlbum("Hollow Crown");

        LibraryRecord third = new LibraryRecord();
        third.setTitle("Digital Love");
        third.setArtist("Daft Punk");
        third.setAlbum("Discovery");

        searchService.addRecords(List.of(first, second, third));

        List<LibraryRecord> results = searchService.searchByArtist("Architects");
        System.out.println("SearchServiceTest artist results: " + results.size());

        assertEquals(2, results.size());
        assertEquals("Early Grave", results.get(0).getTitle());
        assertEquals("Dethroned", results.get(1).getTitle());
    }

    @Test
    void searchByAlbumReturnsMatchingRecords() {
        LibraryIndex index = new LibraryIndex();
        SearchService searchService = new SearchService(index);

        searchService.addRecords(List.of(
            record("Early Grave", "Architects", "Hollow Crown"),
            record("Dethroned", "Architects", "Hollow Crown"),
            record("Digital Love", "Daft Punk", "Discovery")
        ));

        List<LibraryRecord> results = searchService.searchByAlbum("discovery");
        System.out.println("SearchServiceTest album result: " + results.get(0).getTitle());

        assertEquals(1, results.size());
        assertEquals("Digital Love", results.get(0).getTitle());
    }

    @Test
    void replaceRecordsClearsOldDataBeforeAddingNewData() {
        LibraryIndex index = new LibraryIndex();
        SearchService searchService = new SearchService(index);

        searchService.addRecords(List.of(
            record("Early Grave", "Architects", "Hollow Crown"),
            record("Dethroned", "Architects", "Hollow Crown")
        ));

        searchService.replaceRecords(List.of(
            record("Digital Love", "Daft Punk", "Discovery")
        ));
        System.out.println("SearchServiceTest records after replace: " + searchService.getAllRecords().size());

        assertEquals(1, searchService.getAllRecords().size());
        assertTrue(searchService.searchByArtist("Architects").isEmpty());
        assertEquals("Digital Love", searchService.getAllRecords().get(0).getTitle());
    }

    private LibraryRecord record(String title, String artist, String album) {
        LibraryRecord record = new LibraryRecord();
        record.setTitle(title);
        record.setArtist(artist);
        record.setAlbum(album);
        return record;
    }
}
