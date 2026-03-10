package com.caleb.musiclibrary;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.caleb.musiclibrary.util.ConsolePrinter;
import com.caleb.musiclibrary.util.PathUtils;

/**
 * Entry point for the Music Library application and top-level workflow orchestration.
 */
public class App {
    // this is for whether or not im on my desktop or laptop, which have different drive letters for the music library.
    private static final Path ROOT = Files.isDirectory(Paths.get("E:\\! Ripped Music")) ? Paths.get("E:\\! Ripped Music") : Paths.get("D:\\! Ripped Music");

    private final ConsolePrinter printer = new ConsolePrinter();
    private final PathUtils pathUtils = new PathUtils();

    public static void main(String[] args) {
        new App().run();
    }

    private void run() {
        if (!Files.isDirectory(ROOT)) {
            System.err.println("Library root was not found: " + ROOT);
            return;
        }

        Scanner scanner = new Scanner(System.in);
        browseLibrary(scanner);
    }

    private void browseLibrary(Scanner scanner) {
        while (true) {
            Path letterFolder = chooseDirectory(ROOT, "Browse Letters", scanner, false);
            if (letterFolder == null) {
                return;
            }

            while (true) {
                Path artistFolder = chooseDirectory(letterFolder, "Browse Artists", scanner, true);
                if (artistFolder == null) {
                    break;
                }

                while (true) {
                    Path albumFolder = chooseDirectory(artistFolder, "Browse Albums", scanner, true);
                    if (albumFolder == null) {
                        break;
                    }

                    showAlbumTracks(albumFolder, scanner);
                }
            }
        }
    }

    private Path chooseDirectory(Path parent, String title, Scanner scanner, boolean allowBack) {
        while (true) {
            List<Path> directories = listDirectories(parent);

            printer.printHeader(title);
            printer.printCurrentPath(parent);

            if (directories.isEmpty()) {
                printer.printMessage("No folders found here.");
                return null;
            }

            printer.printNumberedPaths(directories);
            printer.printNavigationPrompt(allowBack);

            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                System.exit(0);
            }
            if (allowBack && input.equalsIgnoreCase("b")) {
                return null;
            }

            Integer index = parseSelection(input, directories.size());
            if (index != null) {
                return directories.get(index);
            }

            printer.printMessage("Invalid choice. Please try again.");
        }
    }

    private void showAlbumTracks(Path albumFolder, Scanner scanner) {
        List<Path> tracks = listTracks(albumFolder);

        printer.printHeader("Album Tracks");
        printer.printCurrentPath(albumFolder);

        if (tracks.isEmpty()) {
            printer.printMessage("No supported audio files were found in this album folder.");
        } else {
            printer.printTracks(tracks);
        }

        System.out.print("Press Enter to go back, or type 'q' to quit: ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("q")) {
            System.exit(0);
        }
    }

    private Integer parseSelection(String input, int size) {
        if (!input.matches("\\d+")) {
            return null;
        }

        int selected = Integer.parseInt(input);
        if (selected < 1 || selected > size) {
            return null;
        }
        return selected - 1;
    }

    private List<Path> listDirectories(Path parent) {
        File[] files = parent.toFile().listFiles(File::isDirectory);
        if (files == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(files)
            .map(File::toPath)
            .sorted(Comparator.comparing(path -> path.getFileName().toString().toLowerCase()))
            .collect(Collectors.toList());
    }

    private List<Path> listTracks(Path albumFolder) {
        File[] files = albumFolder.toFile().listFiles(File::isFile);
        if (files == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(files)
            .map(File::toPath)
            .filter(pathUtils::isAudioFile)
            .sorted(Comparator.comparing(path -> path.getFileName().toString().toLowerCase()))
            .collect(Collectors.toList());
    }
}
