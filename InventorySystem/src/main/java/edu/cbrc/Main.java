package org.example;

import java.sql.*;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:sqlite:users.db";
        String ddl = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    userName TEXT NOT NULL,
                    userPassword TEXT NOT NULL,
                    accessPermission TEXT NOT NULL
                )
                """;

        try (Connection conn = DriverManager.getConnection(url)) {
            try (Statement st = conn.createStatement()) {
                st.execute(ddl);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        User user = User.logIn();
        if (user != null) {
            user.displayAccessPortal();
        }
    }
}