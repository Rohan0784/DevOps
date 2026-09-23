package com.napier.devops;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    /** Connection to the MySQL database. */
    private Connection con = null;

    public static void main(String[] args) {
        App a = new App();
        a.connect();
        a.disconnect();
    }

    /** Connect to the MySQL database. */
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load SQL driver", e);
        }

        int retries = 10;
        for (int attempt = 1; attempt <= retries; ++attempt) {
            System.out.println("Connecting to database...");
            try {
                // Allow time for MySQL to initialize and import the employee data.
                Thread.sleep(30000);
                // Public key retrieval supports MySQL 8.4 authentication in this local lab.
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?useSSL=false&allowPublicKeyRetrieval=true",
                        "root", "example");
                System.out.println("Successfully connected");
                return;
            } catch (SQLException e) {
                System.out.println("Failed to connect to database attempt " + attempt);
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Connection attempt interrupted", e);
            }
        }

        throw new IllegalStateException("Could not connect to database after " + retries + " attempts");
    }

    /** Disconnect from the MySQL database. */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                throw new IllegalStateException("Error closing connection to database", e);
            }
        }
    }
}
