package org.example.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDataBase implements AutoCloseable {
    private final String url = System.getenv("DB_URL");
    private final String user = System.getenv("DB_USER");
    private final String password = System.getenv("DB_PASSWORD");
    private final String jdbc;
    private Connection connection;

    public ConnectionDataBase() {
        if (url == null || user == null || password == null) {
            throw new IllegalStateException("Database connection details are missing in environment variables.");
        }
        jdbc = url;
    }

    public Connection getConnection() {
        try {
            // Check if the connection is closed or null, and create a new one if necessary
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(jdbc, user, password);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection.", e);
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // Log or handle exception if needed
            throw new RuntimeException("Failed to close the database connection.", e);
        }
    }
}
