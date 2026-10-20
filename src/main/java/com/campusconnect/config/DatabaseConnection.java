
package com.campusconnect.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://campusconnect-db-adityakumarpandit643-8de0.f.aivencloud.com:22327/campusconnect?sslMode=REQUIRED";

    private static final String USER =
            System.getenv("CAMPUSCONNECT_DB_USER");

    private static final String PASSWORD =
            System.getenv("CAMPUSCONNECT_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        if (USER == null || PASSWORD == null
                || USER.isBlank() || PASSWORD.isBlank()) {
            throw new SQLException(
                    "Database credentials are not configured."
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {

        try (Connection connection = getConnection()) {
            System.out.println("Database Connected Successfully!");
        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }
    }
}
