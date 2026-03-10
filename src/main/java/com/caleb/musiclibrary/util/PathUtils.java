package com.caleb.musiclibrary.util;

import java.nio.file.Path;

/**
 * Provides helper methods for path normalization and audio-file checks.
 */
public class PathUtils {
    public boolean isAudioFile(Path path) {
        // the only audio files we care about are mp3, there are no other file types in my library.
        String name = path.getFileName().toString().toLowerCase();
        return name.endsWith(".mp3")
            && !name.startsWith("."); // ignore hidden files like .DS_Store
    }
}
