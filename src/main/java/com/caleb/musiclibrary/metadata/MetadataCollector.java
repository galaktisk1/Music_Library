package com.caleb.musiclibrary.metadata;

/**
 * Coordinates metadata extraction from audio files for downstream indexing and storage.
 */

import java.nio.file.Path;
import java.util.Map;
import com.caleb.musiclibrary.model.LibraryRecord;
public class MetadataCollector {
    private TagReader tagReader;

    public MetadataCollector() {
        this.tagReader = new TagReader();
    }

    public LibraryRecord collect(Path file) {
        Map<String, String> tags = tagReader.readTags(file);
        LibraryRecord record = new LibraryRecord();
        record.setFilePath(tags.get("filePath"));
        record.setTitle(tags.get("title"));
        record.setArtist(tags.get("artist"));
        record.setAlbum(tags.get("album"));
        record.setYear(parseInt(tags.get("year")));
        record.setTrackNumber(parseInt(tags.get("trackNumber")));
        return record;
    }

    private Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
