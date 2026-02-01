package repository.interfaces;

import model.Playlist;

import java.util.List;

public interface PlaylistRepository extends CrudRepository<Playlist> {
    void addMediaToPlaylist(int playlistId, int mediaId);
    void removeMediaFromPlaylist(int playlistId, int mediaId);
    List<Integer> getMediaIds(int playlistId);
    boolean existsByName(String name);
}
