package com.caleb.musiclibrary;

import com.caleb.musiclibrary.controller.LibraryController;
import com.caleb.musiclibrary.index.LibraryIndex;
import com.caleb.musiclibrary.index.SearchService;
import com.caleb.musiclibrary.scan.LibraryScanner;
import com.caleb.musiclibrary.ui.MainWindow;

/**
 * Future graphical entry point for the Music Library application.
 */
public class MusicLibraryApp {
    public static void main(String[] args) {
        new MusicLibraryApp().start();
    }

    public void start() {
        LibraryIndex index = new LibraryIndex();
        SearchService searchService = new SearchService(index);
        LibraryScanner scanner = new LibraryScanner();
        LibraryController controller = new LibraryController(scanner, searchService);
        MainWindow mainWindow = new MainWindow(controller);

        mainWindow.showHomeScreen();
    }
}
