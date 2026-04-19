package com.caleb.musiclibrary.ui;

import com.caleb.musiclibrary.controller.LibraryController;
import com.caleb.musiclibrary.model.LibraryRecord;

/**
 * Placeholder for the future graphical user interface screens.
 */
public class MainWindow {
    private final LibraryController controller;

    public MainWindow(LibraryController controller) {
        this.controller = controller;
    }

    public void showHomeScreen() {
        // GUI controls will call the controller from this screen.
    }

    public void showScanScreen() {
        // The scan screen will ask the user to choose a music folder.
    }

    public void showSearchScreen() {
        // The search screen will collect title, artist, or album search input.
    }

    public void showResultsScreen() {
        // The results screen will display matching LibraryRecord rows.
    }

    public void showTrackDetails(LibraryRecord record) {
        // The detail screen will display one selected track record.
    }

    public LibraryController getController() {
        return controller;
    }
}
