package com.greenloop.dao;

import com.greenloop.database.DBConnection;
import com.greenloop.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    // Get all products with stock info
    public List<Product> getAllStock() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("eco_rating"),
                        rs.getInt("quantity_on_hand"),
                        rs.getInt("reorder_level")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get low stock products (quantity below reorder level)
    public List<Product> getLowStockProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE quantity_on_hand <= reorder_level";
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("eco_rating"),
                        rs.getInt("quantity_on_hand"),
                        rs.getInt("reorder_level")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Stock in from supplier (increase quantity)
    public boolean stockIn(int productId, int quantity) {
        String sql = "UPDATE products SET quantity_on_hand = quantity_on_hand + ? WHERE product_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update reorder level
    public boolean updateReorderLevel(int productId, int reorderLevel) {
        String sql = "UPDATE products SET reorder_level = ? WHERE product_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reorderLevel);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
