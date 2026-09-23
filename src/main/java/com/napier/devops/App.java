package com.napier.devops;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not load SQL driver");
            System.exit(1);
        }

        int retries = 100;
        for (int attempt = 1; attempt <= retries; ++attempt) {
            System.out.println("Connecting to database...");
            try {
                // Allow time for MySQL to initialize and import the employee data.
                Thread.sleep(30000);
                // Public key retrieval supports MySQL 8.4 authentication in this local lab.
                try (Connection con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?useSSL=false&allowPublicKeyRetrieval=true",
                        "root", "example")) {
                    System.out.println("Successfully connected");
                    Thread.sleep(10000);
                }
                return;
            } catch (SQLException e) {
                System.out.println("Failed to connect to database attempt " + attempt);
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Connection attempt interrupted");
                System.exit(1);
            }
        }

        System.err.println("Could not connect to database after " + retries + " attempts");
        System.exit(1);
    }
}
