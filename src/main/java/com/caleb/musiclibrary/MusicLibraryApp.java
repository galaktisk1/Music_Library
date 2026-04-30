package com.caleb.musiclibrary;

import com.caleb.musiclibrary.controller.LibraryController;
import com.caleb.musiclibrary.index.LibraryIndex;
import com.caleb.musiclibrary.index.SearchService;
import com.caleb.musiclibrary.scan.LibraryScanner;
import com.caleb.musiclibrary.ui.MainWindow;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX application startup for the Music Library UI.
 */
public class MusicLibraryApp extends Application {
    public static void launchApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LibraryIndex index = new LibraryIndex();
        SearchService searchService = new SearchService(index);
        LibraryScanner scanner = new LibraryScanner();
        LibraryController controller = new LibraryController(scanner, searchService);
        MainWindow mainWindow = new MainWindow(controller, primaryStage);

        mainWindow.showHomeScreen();
    }
}
