package com.caleb.musiclibrary;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

import com.caleb.musiclibrary.scan.FileWalker;

/**
 * Entry point for the Music Library application and top-level workflow orchestration.
 */
public class App {
    public static void main(String[] args) {
        Path root = Paths.get("E:\\! Ripped Music");
        FileWalker walker = new FileWalker();

        try {
            List<Path> files = walker.findAudioFiles(root).stream()
                .sorted()
                .toList();
            System.out.println("Scan root: " + root);
            System.out.println("Audio files found: " + files.size());
            files.stream().limit(10).forEach(path -> System.out.println(" - " + path));
        } catch (IOException e) {
            System.err.println("Failed to scan music folder: " + e.getMessage());
        }
    }
}
