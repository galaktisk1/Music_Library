package com.caleb.musiclibrary.util;

import java.nio.file.Path;
import java.util.List;

/**
 * Centralizes formatted console output for the CLI experience.
 */
public class ConsolePrinter {
    public void printHeader(String title) {
        System.out.println();
        System.out.println("== " + title + " ==");
    }

    public void printCurrentPath(Path path) {
        System.out.println("Current folder: " + path);
    }

    public void printNumberedPaths(List<Path> paths) {
        for (int i = 0; i < paths.size(); i++) {
            System.out.println((i + 1) + ". " + paths.get(i).getFileName());
        }
    }

    public void printTracks(List<Path> tracks) {
        for (int i = 0; i < tracks.size(); i++) {
            System.out.println((i + 1) + ". " + tracks.get(i).getFileName());
        }
    }

    public void printNavigationPrompt(boolean allowBack) {
        if (allowBack) {
            System.out.print("Choose a number, 'b' to go back, or 'q' to quit: ");
            return;
        }
        System.out.print("Choose a number or 'q' to quit: ");
    }

    public void printMessage(String message) {
        System.out.println(message);
    }
}
