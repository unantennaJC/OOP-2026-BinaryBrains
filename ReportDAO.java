package com.greenloop.dao;

import com.greenloop.database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    // Get monthly revenue summary
    public List<String[]> getMonthlySummary() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT DATE_FORMAT(order_date, '%Y-%m') as month, " +
                "COUNT(*) as total_orders, " +
                "SUM(total_amount) as total_revenue " +
                "FROM orders " +
                "WHERE status != 'Cancelled' " +
                "GROUP BY DATE_FORMAT(order_date, '%Y-%m') " +
                "ORDER BY month DESC";
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("month"),
                        rs.getString("total_orders"),
                        rs.getString("total_revenue")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get low stock products
    public List<String[]> getLowStockReport() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT name, quantity_on_hand, reorder_level " +
                "FROM products " +
                "WHERE quantity_on_hand <= reorder_level " +
                "ORDER BY quantity_on_hand ASC";
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("name"),
                        rs.getString("quantity_on_hand"),
                        rs.getString("reorder_level")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get top selling products
    public List<String[]> getTopSellingProducts() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT p.name, SUM(oi.quantity) as total_sold, " +
                "SUM(oi.quantity * oi.unit_price) as total_revenue " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "GROUP BY p.product_id, p.name " +
                "ORDER BY total_sold DESC";
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("name"),
                        rs.getString("total_sold"),
                        rs.getString("total_revenue")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get total revenue
    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) as total FROM orders WHERE status != 'Cancelled'";
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getDouble("total");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Get total orders count
    public int getTotalOrders() {
        String sql = "SELECT COUNT(*) as total FROM orders";
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get total clients count
    public int getTotalClients() {
        String sql = "SELECT COUNT(*) as total FROM clients";
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
