package com.campuskart;

import java.sql.*;

public final class DBConnection {
    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }

        return DriverManager.getConnection(
            System.getenv().getOrDefault(
                "CAMPUSKART_DB_URL",
                "jdbc:mysql://localhost:3306/campuskart?useSSL=false&serverTimezone=Asia/Kolkata"
            ),
            System.getenv().getOrDefault("CAMPUSKART_DB_USER", "root"),
            System.getenv().getOrDefault("CAMPUSKART_DB_PASSWORD", "")
        );
    }
}
