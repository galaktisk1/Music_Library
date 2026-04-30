package com.caleb.musiclibrary.index;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        assertEquals(2, results.size());
        assertEquals("Early Grave", results.get(0).getTitle());
        assertEquals("Dethroned", results.get(1).getTitle());
    }
}
