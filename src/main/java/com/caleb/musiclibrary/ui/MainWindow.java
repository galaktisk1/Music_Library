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
        // TODO: Display the home screen and connect controls to the controller.
    }

    public void showScanScreen() {
        // TODO: Ask the user to choose a music folder.
    }

    public void showSearchScreen() {
        // TODO: Collect title, artist, or album search input.
        // TODO: A track search should open the containing album instead of
        // showing the track by itself.
    }

    public void showResultsScreen() {
        // TODO: Show matching albums or tracks.
        // TODO: Highlight a matched track inside its album track list.
    }

    public void showTrackDetails(LibraryRecord record) {
        // TODO: Display one selected track record.
    }

    public LibraryController getController() {
        return controller;
    }
}
