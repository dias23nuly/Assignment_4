package repository.jdbc;

import exception.DatabaseOperationException;
import model.Playlist;
import repository.interfaces.PlaylistRepository;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPlaylistRepository implements PlaylistRepository {

    @Override
    public Playlist create(Playlist entity) {
        String sql = "INSERT INTO playlists(name) VALUES (?) RETURNING playlist_id";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) entity.setId(rs.getInt(1));
            return entity;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Create playlist failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Playlist> findById(int id) {
        String sql = "SELECT playlist_id, name FROM playlists WHERE playlist_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();
            return Optional.of(new Playlist(rs.getInt("playlist_id"), rs.getString("name")));
        } catch (SQLException e) {
            throw new DatabaseOperationException("Find playlist failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Playlist> findAll() {
        String sql = "SELECT playlist_id, name FROM playlists ORDER BY playlist_id";
        List<Playlist> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) out.add(new Playlist(rs.getInt("playlist_id"), rs.getString("name")));
            return out;
        } catch (SQLException e) {
            throw new DatabaseOperationException("List playlists failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Playlist update(Playlist entity) {
        String sql = "UPDATE playlists SET name=? WHERE playlist_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getId());
            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Update playlist failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM playlists WHERE playlist_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Delete playlist failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void addMediaToPlaylist(int playlistId, int mediaId) {
        String sql = "INSERT INTO playlist_items(playlist_id, media_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, mediaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Add media to playlist failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeMediaFromPlaylist(int playlistId, int mediaId) {
        String sql = "DELETE FROM playlist_items WHERE playlist_id=? AND media_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, mediaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Remove media from playlist failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Integer> getMediaIds(int playlistId) {
        String sql = "SELECT media_id FROM playlist_items WHERE playlist_id=? ORDER BY media_id";
        List<Integer> ids = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ids.add(rs.getInt("media_id"));
            return ids;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Get playlist items failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM playlists WHERE name=? LIMIT 1";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Exists playlist failed: " + e.getMessage(), e);
        }
    }
}
