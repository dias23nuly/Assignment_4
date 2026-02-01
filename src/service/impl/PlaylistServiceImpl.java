package service.impl;

import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import repository.interfaces.MediaRepository;
import repository.interfaces.PlaylistRepository;
import service.interfaces.PlaylistService;
import model.Playlist;

import java.util.List;

public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepo; // DIP
    private final MediaRepository mediaRepo;       // DIP (чтобы проверять, что media существует)

    public PlaylistServiceImpl(PlaylistRepository playlistRepo, MediaRepository mediaRepo) {
        this.playlistRepo = playlistRepo;
        this.mediaRepo = mediaRepo;
    }

    @Override
    public Playlist create(Playlist playlist) {
        if (playlist == null || playlist.getName() == null || playlist.getName().trim().isEmpty()) {
            throw new InvalidInputException("Playlist name is blank");
        }
        if (playlistRepo.existsByName(playlist.getName())) {
            throw new InvalidInputException("Playlist already exists: " + playlist.getName());
        }
        return playlistRepo.create(playlist);
    }

    @Override
    public Playlist getById(int id) {
        return playlistRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found: id=" + id));
    }

    @Override
    public List<Playlist> getAll() {
        return playlistRepo.findAll();
    }

    @Override
    public Playlist update(Playlist playlist) {
        if (playlist.getId() <= 0) throw new InvalidInputException("playlist.id must be > 0 for update");
        playlistRepo.findById(playlist.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found: id=" + playlist.getId()));
        if (playlist.getName() == null || playlist.getName().trim().isEmpty()) {
            throw new InvalidInputException("Playlist name is blank");
        }
        return playlistRepo.update(playlist);
    }

    @Override
    public void delete(int id) {
        playlistRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Playlist not found: id=" + id));
        playlistRepo.delete(id); // CASCADE удалит playlist_items
    }

    @Override
    public void addMedia(int playlistId, int mediaId) {
        playlistRepo.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found: id=" + playlistId));
        mediaRepo.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found: id=" + mediaId));

        playlistRepo.addMediaToPlaylist(playlistId, mediaId);
    }

    @Override
    public void removeMedia(int playlistId, int mediaId) {
        playlistRepo.removeMediaFromPlaylist(playlistId, mediaId);
    }

    @Override
    public List<Integer> listMediaIds(int playlistId) {
        playlistRepo.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found: id=" + playlistId));
        return playlistRepo.getMediaIds(playlistId);
    }
}
