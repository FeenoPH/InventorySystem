package edu.cbrc;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class oInsert implements Operation {

    private final double operationPermission = 5;
    private final String operationName = "INSERT";
    private final String operationDescription = "Insert new item into inventory";

    @Override
    public double getPermission() {
        return operationPermission;
    }

    @Override
    public String getName() {
        return operationName;
    }

    @Override
    public String getDescription() {
        return operationDescription;
    }

    @Override
    public int run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the SKU of the item you want to insert: ");
        String itemSKUInput = sc.nextLine();
        int itemQuantityInput;
        while (true) {
            System.out.println("Please enter the quantity of the item you want to insert: ");
            try {
                itemQuantityInput = sc.nextInt();
                if (itemQuantityInput < 1) {
                    throw new InputMismatchException();
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid quantity!");
            }
        }
        String url = "jdbc:sqlite:inventory.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inventory WHERE itemSku = ?")) {
                ps.setString(1, itemSKUInput);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    sc = new Scanner(System.in);
                    System.out.println("Please enter the name of the item you want to insert: ");
                    String itemNameInput = sc.nextLine();
                    double itemPriceInput;
                    while (true) {
                        System.out.println("Please enter the price of the item you want to insert (single unit): ");
                        try {
                            itemPriceInput = sc.nextDouble();
                            if (itemPriceInput < 1) {
                                throw new InputMismatchException();
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid price!");
                        }
                    }
                    try (PreparedStatement ps2 = conn.prepareStatement("INSERT INTO inventory(itemName, itemSku, itemQuantity, itemPrice) VALUES (?, ?, ?, ?)")) {
                        ps2.setString(1, itemNameInput);
                        ps2.setString(2, itemSKUInput);
                        ps2.setInt(3, itemQuantityInput);
                        ps2.setDouble(4, itemPriceInput);
                        ps2.executeUpdate();
                    }
                    conn.commit();
                    System.out.println("Item inserted successfully!");
                    return 0;
                }
                try (PreparedStatement ps3 = conn.prepareStatement("UPDATE inventory SET itemQuantity = ? WHERE itemSku = ?")) {
                    ps3.setInt(1, itemQuantityInput+rs.getInt("itemQuantity"));
                    ps3.setString(2, itemSKUInput);
                    ps3.executeUpdate();
                }
                conn.commit();
                System.out.println("Item inserted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            return 1;
        }
        return 0;
    }
}
