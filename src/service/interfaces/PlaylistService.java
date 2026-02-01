package service.interfaces;

import model.Playlist;

import java.util.List;

public interface PlaylistService {
    Playlist create(Playlist playlist);
    Playlist getById(int id);
    List<Playlist> getAll();
    Playlist update(Playlist playlist);
    void delete(int id);

    void addMedia(int playlistId, int mediaId);
    void removeMedia(int playlistId, int mediaId);
    List<Integer> listMediaIds(int playlistId);
}
