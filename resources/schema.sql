DROP TABLE IF EXISTS playlist_items;
DROP TABLE IF EXISTS playlists;
DROP TABLE IF EXISTS media;
DROP TABLE IF EXISTS categories;

CREATE TABLE categories (
                            category_id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE media (
                       media_id SERIAL PRIMARY KEY,
                       name VARCHAR(200) NOT NULL,
                       type VARCHAR(20) NOT NULL CHECK (type IN ('SONG', 'PODCAST')),
                       duration_seconds INT NOT NULL CHECK (duration_seconds > 0),
                       price_kzt INT NOT NULL DEFAULT 0 CHECK (price_kzt >= 0),
                       category_id INT NOT NULL REFERENCES categories(category_id)
);

CREATE TABLE playlists (
                           playlist_id SERIAL PRIMARY KEY,
                           name VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE playlist_items (
                                playlist_id INT NOT NULL,
                                media_id INT NOT NULL,
                                PRIMARY KEY (playlist_id, media_id),
                                FOREIGN KEY (playlist_id) REFERENCES playlists(playlist_id) ON DELETE CASCADE,
                                FOREIGN KEY (media_id) REFERENCES media(media_id) ON DELETE RESTRICT
);

INSERT INTO categories(name) VALUES ('Pop'), ('Hip-Hop'), ('Education');
