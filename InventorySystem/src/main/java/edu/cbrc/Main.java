package edu.cbrc;

import java.sql.*;


public class Main {

    public static void main(String[] args) {

        String url = "jdbc:sqlite:inventory.db";
        String ddl1 = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    userName TEXT NOT NULL,
                    userPassword TEXT NOT NULL,
                    accessPermission REAL NOT NULL
                )
                """;
        String ddl2 = """
                CREATE TABLE IF NOT EXISTS inventory (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    itemName TEXT NOT NULL,
                    itemSku TEXT NOT NULL,
                    itemQuantity INTEGER NOT NULL,
                    itemPrice REAL NOT NULL
                )
                """;

        try (Connection conn = DriverManager.getConnection(url)) {
            try (Statement st = conn.createStatement()) {
                st.execute(ddl1);
                st.execute(ddl2);
                st.execute("INSERT INTO users (userName, userPassword, accessPermission) VALUES ('admin', 'admin50', 10)");
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