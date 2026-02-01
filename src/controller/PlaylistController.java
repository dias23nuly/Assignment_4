package controller;

import model.Playlist;
import service.interfaces.PlaylistService;

import java.util.List;

public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    public Playlist create(Playlist p) { return playlistService.create(p); }
    public Playlist getById(int id) { return playlistService.getById(id); }
    public List<Playlist> getAll() { return playlistService.getAll(); }
    public Playlist update(Playlist p) { return playlistService.update(p); }
    public void delete(int id) { playlistService.delete(id); }

    public void addMedia(int playlistId, int mediaId) { playlistService.addMedia(playlistId, mediaId); }
    public void removeMedia(int playlistId, int mediaId) { playlistService.removeMedia(playlistId, mediaId); }
    public List<Integer> listMediaIds(int playlistId) { return playlistService.listMediaIds(playlistId); }
}
