package repository.jdbc;

import exception.DatabaseOperationException;
import model.*;
import repository.interfaces.MediaRepository;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMediaRepository implements MediaRepository {

    @Override
    public MediaContentBase create(MediaContentBase entity) {
        String sql = """
                INSERT INTO media(name, type, duration_seconds, price_kzt, category_id)
                VALUES (?, ?, ?, ?, ?)
                RETURNING media_id
                """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getType().name());
            ps.setInt(3, entity.getDurationSeconds());
            ps.setInt(4, entity.getPriceKzt());
            ps.setInt(5, entity.getCategory().getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) entity.setId(rs.getInt(1));
            return entity;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Create media failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<MediaContentBase> findById(int id) {
        String sql = """
                SELECT m.media_id, m.name, m.type, m.duration_seconds, m.price_kzt,
                       c.category_id, c.name AS category_name
                FROM media m
                JOIN categories c ON c.category_id = m.category_id
                WHERE m.media_id = ?
                """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();
            return Optional.of(mapMedia(rs));
        } catch (SQLException e) {
            throw new DatabaseOperationException("Find media failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MediaContentBase> findAll() {
        String sql = """
                SELECT m.media_id, m.name, m.type, m.duration_seconds, m.price_kzt,
                       c.category_id, c.name AS category_name
                FROM media m
                JOIN categories c ON c.category_id = m.category_id
                ORDER BY m.media_id
                """;
        List<MediaContentBase> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) out.add(mapMedia(rs));
            return out;
        } catch (SQLException e) {
            throw new DatabaseOperationException("List media failed: " + e.getMessage(), e);
        }
    }

    @Override
    public MediaContentBase update(MediaContentBase entity) {
        String sql = """
                UPDATE media
                SET name=?, duration_seconds=?, price_kzt=?, category_id=?
                WHERE media_id=?
                """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getDurationSeconds());
            ps.setInt(3, entity.getPriceKzt());
            ps.setInt(4, entity.getCategory().getId());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Update media failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM media WHERE media_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Delete media failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByNameAndType(String name, MediaType type) {
        String sql = "SELECT 1 FROM media WHERE name=? AND type=? LIMIT 1";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, type.name());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Exists media failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<MediaContentBase> findByNameAndType(String name, MediaType type) {
        String sql = """
                SELECT m.media_id, m.name, m.type, m.duration_seconds, m.price_kzt,
                       c.category_id, c.name AS category_name
                FROM media m
                JOIN categories c ON c.category_id = m.category_id
                WHERE m.name=? AND m.type=?
                """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, type.name());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();
            return Optional.of(mapMedia(rs));
        } catch (SQLException e) {
            throw new DatabaseOperationException("Find media by name/type failed: " + e.getMessage(), e);
        }
    }

    private MediaContentBase mapMedia(ResultSet rs) throws SQLException {
        Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
        MediaType type = MediaType.valueOf(rs.getString("type"));

        // OCP/LSP: легко добавлять новые subtype без правки логики сервиса (тут только маппинг)
        MediaContentBase m = switch (type) {
            case SONG -> new Song(rs.getString("name"), rs.getInt("duration_seconds"),
                    rs.getInt("price_kzt"), category, null);
            case PODCAST -> new Podcast(rs.getString("name"), rs.getInt("duration_seconds"),
                    rs.getInt("price_kzt"), category, null);
        };
        m.setId(rs.getInt("media_id"));
        return m;
    }
}
