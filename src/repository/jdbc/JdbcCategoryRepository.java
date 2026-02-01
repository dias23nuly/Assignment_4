package repository.jdbc;

import exception.DatabaseOperationException;
import model.Category;
import repository.interfaces.CategoryRepository;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCategoryRepository implements CategoryRepository {

    @Override
    public Category create(Category entity) {
        String sql = "INSERT INTO categories(name) VALUES (?) RETURNING category_id";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) entity.setId(rs.getInt(1));
            return entity;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Create category failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Category> findById(int id) {
        String sql = "SELECT category_id, name FROM categories WHERE category_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();
            return Optional.of(new Category(rs.getInt("category_id"), rs.getString("name")));
        } catch (SQLException e) {
            throw new DatabaseOperationException("Find category failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT category_id, name FROM categories ORDER BY category_id";
        List<Category> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new Category(rs.getInt("category_id"), rs.getString("name")));
            }
            return out;
        } catch (SQLException e) {
            throw new DatabaseOperationException("List categories failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Category update(Category entity) {
        String sql = "UPDATE categories SET name=? WHERE category_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getId());
            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Update category failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM categories WHERE category_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Delete category failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Category> findByName(String name) {
        String sql = "SELECT category_id, name FROM categories WHERE name=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();
            return Optional.of(new Category(rs.getInt("category_id"), rs.getString("name")));
        } catch (SQLException e) {
            throw new DatabaseOperationException("Find category by name failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM categories WHERE name=? LIMIT 1";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Exists category failed: " + e.getMessage(), e);
        }
    }
}
