package edu.cbrc;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class oDisplay implements Operation {

    private final double operationPermission = 1;
    private final String operationName = "DISPLAY";
    private final String operationDescription = "Display all item in inventory";

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
        int sortMode;
        int ascDesc = 0;
        while (true) {
            System.out.println("Please enter how you would like to sort the inventory (1: Item Name, 2: SKU, 3: Quantity, 4: Price, 5: Summary): ");
            Scanner sc = new Scanner(System.in);
            try {
                sortMode = sc.nextInt();
                if (sortMode < 1 || sortMode > 5) {
                    throw new InputMismatchException();
                }
                if (sortMode == 5) {
                    break;
                }
                System.out.println("Please enter your preferred sort order (1: Ascending, 2: Descending: ");
                ascDesc = sc.nextInt();
                if (ascDesc < 1 || ascDesc > 2) {
                    throw new InputMismatchException();
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number");
            }
        }
        String sortModeString = "";
        String sortOrderString = "";
        switch(sortMode) {
            case 1:
                sortModeString = "itemName";
                break;
            case 2:
                sortModeString = "itemSku";
                break;
            case 3:
                sortModeString = "itemQuantity";
                break;
            case 4:
                sortModeString = "itemPrice";
                break;
            default:
                break;
        }
        switch(ascDesc) {
            case 1:
                sortOrderString = "ASC";
                break;
            case 2:
                sortOrderString = "DESC";
                break;
            default:
                break;
        }
        String url = "jdbc:sqlite:inventory.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            if (sortMode == 5) {
                // Implement sortMode 5
                return 0;
            }
            else {
                String sql = "SELECT * FROM inventory ORDER BY " + sortModeString + " " + sortOrderString;
                try (Statement st = conn.createStatement()) {
                    ResultSet rs = st.executeQuery(sql);
                    while (rs.next()) {
                        System.out.println("Item Name: " + rs.getString("itemName") + ", Item Sku: " + rs.getString("ItemSku") + "\nItem Quantity: " + rs.getInt("itemQuantity") + ", Item Price Per Unit: " + rs.getDouble("itemPrice") + "\nItem Price (Total): " + (rs.getDouble("itemPrice") * rs.getInt("itemQuantity")) + "\n");
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error connecting to database: " + ex.getMessage());
            return 1;
        }
        return 0;
    }

}