import sqlite3

conn = sqlite3.connect('cd_database.db')
c = conn.cursor()

for row in c.execute("""
SELECT artists.name, albums.title, albums.year
FROM albums
JOIN artists ON albums.artist_id = artists.id
ORDER BY artists.name, albums.year 
"""):
    print(row)
conn.close()