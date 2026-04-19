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
        // When the user searches for a track, the result should open the album
        // that contains it instead of showing the track by itself.
    }

    public void showResultsScreen() {
        // The results screen will show matching albums or tracks.
        // If a track match is selected, the album track list should be shown
        // with the searched-for track highlighted in that album context.
    }

    public void showTrackDetails(LibraryRecord record) {
        // The detail screen will display one selected track record after the
        // user views it from the highlighted album track list.
    }

    public LibraryController getController() {
        return controller;
    }
}
