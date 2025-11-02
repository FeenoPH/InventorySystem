package org.example;

import java.util.Scanner;
import java.sql.*;

public class User {
    private String userName;
    private String userPassword;
    private String accessPermission;
    private User(String userName, String userPassword, String accessPermission) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.accessPermission = accessPermission;
    }

    public void displayAccessPortal() {
        System.out.println("--" + userName + "'s inventory portal--\n");
    }

    private static User register(String userNameInput) {
        System.out.println("Please create your password: ");
        Scanner sc = new Scanner(System.in);
        String userPasswordInput = sc.nextLine();
        String url = "jdbc:sqlite:users.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO users(userName, userPassword, accessPermission) VALUES (?, ?, ?)")) {
                ps.setString(1, userNameInput);
                ps.setString(2, userPasswordInput);
                ps.setString(3, "minimal");
                ps.executeUpdate();
            }
            conn.commit();
            return new User(userNameInput, userPasswordInput, "minimal");
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            return null;
        }
    }

    public static User logIn() {
        String userPasswordInput;
        System.out.println("Welcome! Please enter your username: ");
        Scanner sc = new Scanner(System.in);
        String userNameInput = sc.nextLine();
        String url = "jdbc:sqlite:users.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE userName = ?")) {
                ps.setString(1, userNameInput);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Please enter your password: ");
                    userPasswordInput = sc.nextLine();
                    if (rs.getString("userPassword").equals(userPasswordInput)) {
                        return new User(userNameInput, userPasswordInput, rs.getString("accessPermission"));
                    }
                    else {
                        System.out.println("Invalid username or password! Please try again.");
                        return logIn();
                    }
                }
                else {
                    conn.close();
                    return register(userNameInput);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            return null;
        }
    }
}
