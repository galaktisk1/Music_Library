package com.caleb.musiclibrary.metadata;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.caleb.musiclibrary.model.Track;

public class MetadataCollectorTest {

    @Test
    void collectReadsMetadataFromOneAudioFile() throws Exception {
        MetadataCollector collector = new MetadataCollector();
        Path file = Paths.get("C:\\Users\\caleb\\OneDrive\\Music\\! USB Backup\\! Ripped Music\\A-B\\Architects\\2009 Hollow Crown\\01 Early Grave.mp3");

        assertTrue(file.toFile().exists(), "Test mp3 file was not found: " + file);

        Track record = collector.collect(file);

        System.out.println("MetadataCollector saw:");
        System.out.println("File Path: " + record.getFilePath());
        System.out.println("Title: " + record.getTitle());
        System.out.println("Artist: " + record.getArtist());
        System.out.println("Album: " + record.getAlbum());
        System.out.println("Year: " + record.getYear());
        System.out.println("Track Number: " + record.getTrackNumber());

        assertNotNull(record);
        assertNotNull(record.getTitle());
        assertNotNull(record.getArtist());
        assertNotNull(record.getAlbum());
    }
}
