package com.caleb.musiclibrary.ui;

import java.io.ByteArrayInputStream;
import java.awt.Desktop;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.caleb.musiclibrary.controller.LibraryController;
import com.caleb.musiclibrary.model.Album;
import com.caleb.musiclibrary.model.Track;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Main JavaFX window for browsing albums and loading tracks on demand.
 */
public class MainWindow {
    private final LibraryController controller;
    private final Stage stage;

    private final ListView<Album> albumList = new ListView<>();
    private final ListView<String> trackList = new ListView<>();
    private final ComboBox<String> searchTypeBox = new ComboBox<>();
    private final TextField searchField = new TextField();
    private final Label albumTitleLabel = new Label("No album selected");
    private final Label albumInfoLabel = new Label("");
    private final Label coverPlaceholder = new Label("No Cover Art");
    private final ImageView coverImageView = new ImageView();
    private final StackPane coverPane = new StackPane();

    private List<Album> currentAlbums = new ArrayList<>();
    private List<Track> currentTracks = new ArrayList<>();
    private String activeTrackSearch = null;

    public MainWindow(LibraryController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    public void showHomeScreen() {
        stage.setTitle("Music Library");

        searchTypeBox.getItems().setAll("Track", "Album", "Artist");
        searchTypeBox.setValue("Album");

        searchField.setPromptText("Search library");
        searchField.setOnAction(event -> runSearch());

        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> runSearch());

        Button scanButton = new Button("Scan Folder");
        scanButton.setOnAction(event -> chooseMusicFolder());

        HBox searchBar = new HBox(8, searchTypeBox, searchField, searchButton, scanButton);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        VBox topArea = new VBox(8, searchBar);
        topArea.setPadding(new Insets(12));

        albumList.setCellFactory(list -> new AlbumCell());
        albumList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                openAlbum(newValue);
            }
        });
        albumList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
                if (selectedAlbum != null) {
                    openAlbumInMediaPlayer(selectedAlbum);
                }
            }
        });

        coverImageView.setFitWidth(240);
        coverImageView.setFitHeight(240);
        coverImageView.setPreserveRatio(true);
        coverPlaceholder.setStyle("-fx-border-color: gray; -fx-padding: 100 50 100 50;");
        coverPane.getChildren().add(coverPlaceholder);

        trackList.setPrefHeight(260);
        trackList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int selectedIndex = trackList.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < currentTracks.size()) {
                    openTrackInMediaPlayer(currentTracks.get(selectedIndex));
                }
            }
        });

        VBox rightPane = new VBox(10, albumTitleLabel, albumInfoLabel, coverPane, trackList);
        rightPane.setPadding(new Insets(10));
        rightPane.setPrefWidth(320);

        BorderPane root = new BorderPane();
        root.setTop(topArea);
        root.setCenter(albumList);
        root.setRight(rightPane);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 1040, 620);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    private void chooseMusicFolder() {
        // I looked up how to open the normal OS folder picker in JavaFX.
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Music Folder");

        File selectedFolder = chooser.showDialog(stage);
        if (selectedFolder == null) {
            return;
        }

        Path folderPath = selectedFolder.toPath();
        List<Album> scannedAlbums = controller.scanFolder(folderPath);

        if (scannedAlbums.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle("Scan Complete");
            alert.setHeaderText("No Albums Found");
            alert.setContentText("No MP3 albums were found in that folder.");
            alert.showAndWait();
            return;
        }

        searchField.clear();
        activeTrackSearch = null;
        updateAlbumList(scannedAlbums);
    }

    private void runSearch() {
        String query = searchField.getText();
        if (query == null) {
            query = "";
        }

        query = query.trim();
        if (query.isEmpty()) {
            showAllAlbums();
            return;
        }

        String searchType = searchTypeBox.getValue();
        List<Album> results;

        if ("Artist".equalsIgnoreCase(searchType)) {
            activeTrackSearch = null;
            results = controller.searchAlbumsByArtist(query);
            updateAlbumList(results);
        } else if ("Track".equalsIgnoreCase(searchType)) {
            activeTrackSearch = query;
            results = controller.searchAlbumsByTrack(query);
            updateAlbumList(results);
        } else {
            activeTrackSearch = null;
            results = controller.searchAlbumsByTitle(query);
            updateAlbumList(results);
        }
    }

    private void showAllAlbums() {
        activeTrackSearch = null;
        updateAlbumList(controller.getAlbums());
    }

    private void updateAlbumList(List<Album> albums) {
        currentAlbums = albums == null ? new ArrayList<>() : new ArrayList<>(albums);
        albumList.getItems().setAll(currentAlbums);

        if (!currentAlbums.isEmpty()) {
            albumList.getSelectionModel().select(0);
        } else {
            albumTitleLabel.setText("No album selected");
            albumInfoLabel.setText("");
            trackList.getItems().clear();
            showCoverArt(null);
        }
    }

    private void openAlbum(Album album) {
        List<Track> tracks = controller.getTracksForAlbum(album);
        currentTracks = tracks;

        albumTitleLabel.setText(safe(album.getTitle()));
        albumInfoLabel.setText(safe(album.getArtist()) + " - " + safeYear(album.getYear()));
        showCoverArt(album.getCoverArt());

        trackList.getItems().clear();
        for (Track track : tracks) {
            String line = "";
            if (track.getTrackNumber() != null) {
                line += track.getTrackNumber() + ". ";
            }
            line += safe(track.getTitle());

            if (activeTrackSearch != null && track.getTitle() != null
                && track.getTitle().toLowerCase().contains(activeTrackSearch.toLowerCase())) {
                line += "  <-- matched track";
            }

            trackList.getItems().add(line);
        }
    }

    private void openTrackInMediaPlayer(Track track) {
        if (track == null || track.getFilePath() == null || track.getFilePath().isBlank()) {
            return;
        }

        try {
            // I looked up how to open a file with the computer's default program.
            Desktop.getDesktop().open(new File(track.getFilePath()));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Open Track");
            alert.setHeaderText("Could not open track");
            alert.setContentText("The selected track could not be opened in the default media player.");
            alert.showAndWait();
        }
    }

    private void openAlbumInMediaPlayer(Album album) {
        if (album == null) {
            return;
        }

        List<Track> tracks = controller.getTracksForAlbum(album);
        if (tracks.isEmpty()) {
            return;
        }

        try {
            List<String> playlistLines = new ArrayList<>();
            playlistLines.add("#EXTM3U");

            for (Track track : tracks) {
                if (track.getFilePath() != null && !track.getFilePath().isBlank()) {
                    playlistLines.add(track.getFilePath());
                }
            }

            Path playlistPath = Files.createTempFile("musiclibrary-album-", ".m3u");
            Files.write(playlistPath, playlistLines, StandardCharsets.UTF_8);
            playlistPath.toFile().deleteOnExit();

            // I looked up the temporary playlist approach so a whole album can open in the default player.
            Desktop.getDesktop().open(playlistPath.toFile());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Open Album");
            alert.setHeaderText("Could not open album");
            alert.setContentText("The selected album could not be opened in the default media player.");
            alert.showAndWait();
        }
    }

    private void showCoverArt(byte[] coverArt) {
        coverPane.getChildren().clear();

        if (coverArt == null || coverArt.length == 0) {
            coverPane.getChildren().add(coverPlaceholder);
            return;
        }

        Image image = new Image(new ByteArrayInputStream(coverArt));
        coverImageView.setImage(image);
        coverPane.getChildren().add(coverImageView);
    }

    private String safe(String value) {
        if (value == null || value.isBlank()) {
            return "Unknown";
        }

        return value;
    }

    private String safeYear(Integer year) {
        if (year == null) {
            return "Unknown Year";
        }

        return String.valueOf(year);
    }

    private class AlbumCell extends ListCell<Album> {
        @Override
        protected void updateItem(Album album, boolean empty) {
            super.updateItem(album, empty);

            if (empty || album == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            ImageView artView = new ImageView();
            artView.setFitWidth(56);
            artView.setFitHeight(56);
            artView.setPreserveRatio(true);

            Label artLabel = new Label("No Art");
            artLabel.setMinWidth(56);
            artLabel.setAlignment(Pos.CENTER);
            artLabel.setStyle("-fx-border-color: gray; -fx-padding: 18 6 18 6;");

            StackPane artPane = new StackPane();
            if (album.getCoverArt() != null && album.getCoverArt().length > 0) {
                artView.setImage(new Image(new ByteArrayInputStream(album.getCoverArt())));
                artPane.getChildren().add(artView);
            } else {
                artPane.getChildren().add(artLabel);
            }

            Label albumLabel = new Label(safe(album.getTitle()));
            Label infoLabel = new Label(safeYear(album.getYear()) + " - " + safe(album.getArtist()));

            VBox textBox = new VBox(4, albumLabel, infoLabel);
            HBox row = new HBox(10, artPane, textBox);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(6));

            setGraphic(row);
            setText(null);
        }
    }
}
