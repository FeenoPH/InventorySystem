package edu.cbrc;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class oRemove implements Operation {

    private final double operationPermission = 7;
    private final String operationName = "REMOVE";
    private final String operationDescription = "Remove pre-existing item from inventory";

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
        System.out.println("Please enter the SKU of the item you want to remove: ");
        String itemSKUInput = sc.nextLine();
        int itemQuantityInput;
        while (true) {
            System.out.println("Please enter the quantity of the item you want to remove: ");
            try {
                itemQuantityInput = sc.nextInt();
                if (itemQuantityInput < 1) {
                    throw new InputMismatchException();
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid quantity!");
                return 0;
            }
        }
        String url = "jdbc:sqlite:inventory.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM inventory WHERE itemSku = ?")) {
                ps.setString(1, itemSKUInput);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    System.out.println("SKU DOES NOT EXIST!");
                    return 0;
                }
                if (rs.getInt("itemQuantity") < itemQuantityInput) {
                    System.out.println("Invalid quantity!");
                    return 0;
                }
                if (rs.getInt("itemQuantity") == itemQuantityInput) {
                    try (PreparedStatement ps2 = conn.prepareStatement("DELETE FROM inventory WHERE itemSku = ?")) {
                        ps2.setString(1, itemSKUInput);
                        ps2.executeUpdate();
                    }
                }
                else {
                    try (PreparedStatement ps2 = conn.prepareStatement("UPDATE inventory SET itemQuantity = ? WHERE itemSku = ?")) {
                        ps2.setInt(1, rs.getInt("itemQuantity")-itemQuantityInput);
                        ps2.setString(2, itemSKUInput);
                        ps2.executeUpdate();
                    }
                }
                conn.commit();
                System.out.println("Item successfully removed!!");
                return 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error connecting to the database: " + ex.getMessage());
            return 0;
        }
    }
}
