package com.caleb.musiclibrary.model;

import java.nio.file.Path;

/**
 * Represents an album in the library browser.
 */
public class Album {
    private String title;
    private String artist;
    private Integer year;
    private Path folderPath;
    private byte[] coverArt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Path getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(Path folderPath) {
        this.folderPath = folderPath;
    }

    public byte[] getCoverArt() {
        return coverArt;
    }

    public void setCoverArt(byte[] coverArt) {
        this.coverArt = coverArt;
    }

    @Override
    public String toString() {
        String yearText = year == null ? "Unknown Year" : String.valueOf(year);
        return yearText + " - " + artist + " - " + title;
    }
}
