package utils;

import exception.DatabaseOperationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            // при необходимости можно раскомментировать
            // Class.forName("org.postgresql.Driver");

            return DriverManager.getConnection(
                    DbConfig.URL,
                    DbConfig.USER,
                    DbConfig.PASSWORD
            );

        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "DB connection failed: " + e.getMessage(), e
            );
        }
    }
}
