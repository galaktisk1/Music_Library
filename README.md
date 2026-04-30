# Music_Library
OO Software Engineering class project

# Original Discussion Board Project
I chose to adapt the project idea posted by Christopher Thompson, who wrote:
“My last Python project was a simple CLI Application to store and search for books I have read before. I implemented the ability to store books with a rating, change book ratings, and search for books.”

# My Adaptation
Instead of storing books, I am building a JavaFX Music Library application that scans my ripped CD collection and creates a searchable library of artists, albums, and tracks.
This project is inspired by the same core idea as the book-tracking application, but adapted to handle a large real-world music collection. I recently ripped hundreds of CDs from my personal collection into a structured folder system, and I want a program that can automatically organize and browse the collection.

# Notes
The music library on my USB drive is the dataset and source of truth for this project.
This project reads metadata directly from those files and displays the collection through a JavaFX interface.
Right now it does not use SQLite, CSV, JSON, or TXT storage.

# Current Features
The current version of the project scans a selected music folder, detects album folders, and builds an album-centered library view in JavaFX.
The app can display album art when it is available in the file metadata, search by track, album, or artist, and load an album's track list when the user selects it.
Tracks can be opened in the computer's default media player, and albums can be opened by creating a temporary playlist file.

# Current Limitations
The project is still in progress and some parts are intentionally simple.
It is currently focused on MP3 files from my own local library structure, and some edge cases in album naming or unusual soundtrack folder layouts are still being refined.
