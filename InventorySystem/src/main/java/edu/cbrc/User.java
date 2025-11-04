package edu.cbrc;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;

public class User {
    private String userName;
    private String userPassword;
    private double accessPermission;
    private User(String userName, String userPassword, double accessPermission) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.accessPermission = accessPermission;
    }

    public void displayAccessPortal() {
        System.out.println("--" + userName + "'s inventory portal--\n");
        ArrayList<Operation> availableOperations = new ArrayList<Operation>();
        Operation[] totalOperations = new Operation[] {new oInsert(), new oRemove(), new oDisplay(), new oExit()};
        for (Operation operation : totalOperations) {
            if (operation.getPermission() <= this.accessPermission) {
                availableOperations.add(operation);
            }
        }
        System.out.println("Please enter the corresponding number to perform the selected action: ");
        int i = 1;
        for (Operation operation : availableOperations) {
            System.out.printf("%d: %s (%s)\n",  i, operation.getName(), operation.getDescription());
            i++;
        }
        Scanner sc = new Scanner(System.in);
        try {
            int userInput = sc.nextInt();
            if (userInput < 1 || userInput > availableOperations.size()) {
                throw new InputMismatchException();
            }
            if (availableOperations.get(userInput-1).run() == 1) {
                return;
            };
        } catch (InputMismatchException e) {
            displayAccessPortal();
        }
        displayAccessPortal();
    }

    private static User register(String userNameInput) {
        System.out.println("Please create your password: ");
        Scanner sc = new Scanner(System.in);
        String userPasswordInput = sc.nextLine();
        String url = "jdbc:sqlite:inventory.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO users(userName, userPassword, accessPermission) VALUES (?, ?, ?)")) {
                ps.setString(1, userNameInput);
                ps.setString(2, userPasswordInput);
                ps.setDouble(3, 3);
                ps.executeUpdate();
            }
            conn.commit();
            return new User(userNameInput, userPasswordInput, 3);
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
        String url = "jdbc:sqlite:inventory.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE userName = ?")) {
                ps.setString(1, userNameInput);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Please enter your password: ");
                    userPasswordInput = sc.nextLine();
                    if (rs.getString("userPassword").equals(userPasswordInput)) {
                        return new User(userNameInput, userPasswordInput, rs.getDouble("accessPermission"));
                    }
                    else {
                        System.out.println("Invalid username or password! Please try again.");
                        conn.close();
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
