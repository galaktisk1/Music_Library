package com.caleb.musiclibrary.scan;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import com.caleb.musiclibrary.metadata.MetadataCollector;
import com.caleb.musiclibrary.model.LibraryRecord;

/**
 * Runs library scanning by combining file discovery and metadata collection.
 */
public class LibraryScanner {
    private final FileWalker fileWalker;
    private final MetadataCollector metadataCollector;

    public LibraryScanner() {
        this(new FileWalker(), new MetadataCollector());
    }

    public LibraryScanner(FileWalker fileWalker, MetadataCollector metadataCollector) {
        this.fileWalker = fileWalker;
        this.metadataCollector = metadataCollector;
    }

    public List<LibraryRecord> scan(Path rootFolder) {
        // TODO: Use FileWalker to find audio files, then use MetadataCollector
        // to turn each file into a LibraryRecord.
        return Collections.emptyList();
    }
}
