package com.example.dentalclinicmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dentalClinic";
    private static final String DB_USER = "root"; // Update with your MySQL username
    private static final String DB_PASSWORD = "BSCSBatch2027"; // Update with your MySQL password

    private static Connection connection;

    // Method to establish a connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connected successfully!");
            } catch (SQLException e) {
                System.err.println("Database connection failed!");
                throw e;
            }
        }
        return connection;
    }

    // Optional: Close the connection (if needed)
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
