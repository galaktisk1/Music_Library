package com.caleb.musiclibrary.scan;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Traverses the filesystem to discover supported audio files.
 */

public class FileWalker {
    public List<Path> findAudioFiles(Path root) {
        List<Path> audioFiles = new ArrayList<>();
        collectAudioFiles(root.toFile(), audioFiles);
        return audioFiles;
    }

    private boolean isAudioFile(Path p) {
        String name = p.getFileName().toString().toLowerCase();
        return name.endsWith(".mp3");
    }

    private void collectAudioFiles(File current, List<Path> audioFiles) {
        File[] files = current.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                collectAudioFiles(file, audioFiles);
            } else if (isAudioFile(file.toPath())) {
                audioFiles.add(file.toPath());
            }
        }
    }
}
