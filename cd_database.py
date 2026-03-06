import sqlite3
import os
import re

ROOT = "D:\\Ripped Music"

conn = sqlite3.connect('cd_database.db')
c = conn.cursor()

c.executescript('''
    CREATE TABLE IF NOT EXISTS artists(
    id INTEGER PRIMARY KEY,
    name TEXT UNIQUE
);
    CREATE TABLE IF NOT EXISTS albums(
    id INTEGER PRIMARY KEY,
    artist_id INTEGER,
    title TEXT,
    year INTEGER,
    path TEXT
);

    CREATE TABLE IF NOT EXISTS tracks(
    id INTEGER PRIMARY KEY,
    album_id INTEGER,
    title TEXT,
    file_path TEXT
);
''')

for artist in os.listdir(ROOT):
    artist_path = os.path.join(ROOT, artist)
    if not os.path.isdir(artist_path):
        continue

    c.execute("INSERT OR IGNORE INTO artists(name) VALUES(?)", (artist,))
    c.execute("SELECT id FROM artists WHERE name=?", (artist,))
    artist_id = c.fetchone()[0]

    for album in os.listdir(artist_path):
        match = re.match(r"\((\d{4})\)\s*(.+)", album)
        if not match:
            continue

        year, album_title = match.groups()
        album_path = os.path.join(artist_path, album)

        c.execute(
            "INSERT INTO albums(artist_id,title,year,path) VALUES(?,?,?,?)",
            (artist_id, album_title, int(year), album_path)
        )

conn.commit()
conn.close()