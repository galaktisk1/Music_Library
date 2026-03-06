package com.caleb.musiclibrary.scan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Traverses the filesystem to discover supported audio files.
 */

public class FileWalker {
    public List<Path> findAudioFiles(Path root) throws IOException {
        try (Stream<Path> stream = Files.walk(root)) {
            return stream
                .filter(Files::isRegularFile)
                .filter(this::isAudioFile)
                .toList();
        }
    }

    private boolean isAudioFile(Path p) {
        String name = p.getFileName().toString().toLowerCase();
        return name.endsWith(".mp3") || name.endsWith(".flac") || name.endsWith(".m4a") || name.endsWith(".wav");
    }

}
