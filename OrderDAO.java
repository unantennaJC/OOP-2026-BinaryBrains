package com.greenloop.dao;

import com.greenloop.database.DBConnection;
import com.greenloop.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Get all orders with client name
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.order_id, o.client_id, c.name as client_name, " +
                "o.order_date, o.status, o.total_amount " +
                "FROM orders o JOIN clients c ON o.client_id = c.client_id";
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Order(
                        rs.getInt("order_id"),
                        rs.getInt("client_id"),
                        rs.getString("client_name"),
                        rs.getString("order_date"),
                        rs.getString("status"),
                        rs.getDouble("total_amount")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Create a new order
    public int createOrder(int clientId) {
        String sql = "INSERT INTO orders (client_id, order_date, status, total_amount) " +
                "VALUES (?, CURDATE(), 'Pending', 0.00)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, clientId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Add product to order
    public boolean addOrderItem(int orderId, int productId, int quantity, double unitPrice) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?,?,?,?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, unitPrice);
            ps.executeUpdate();

            // Update total amount
            updateOrderTotal(orderId);

            // Reduce stock
            String stockSql = "UPDATE products SET quantity_on_hand = quantity_on_hand - ? WHERE product_id = ?";
            PreparedStatement stockPs = conn.prepareStatement(stockSql);
            stockPs.setInt(1, quantity);
            stockPs.setInt(2, productId);
            stockPs.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update order total
    private void updateOrderTotal(int orderId) {
        String sql = "UPDATE orders SET total_amount = " +
                "(SELECT SUM(quantity * unit_price) FROM order_items WHERE order_id = ?) " +
                "WHERE order_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete order
    public boolean deleteOrder(int orderId) {
        try {
            Connection conn = DBConnection.getConnection();
            // Delete order items first
            PreparedStatement ps1 = conn.prepareStatement("DELETE FROM order_items WHERE order_id=?");
            ps1.setInt(1, orderId);
            ps1.executeUpdate();

            // Then delete order
            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM orders WHERE order_id=?");
            ps2.setInt(1, orderId);
            ps2.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get order items for a specific order
    public List<String> getOrderItems(int orderId) {
        List<String> items = new ArrayList<>();
        String sql = "SELECT p.name, oi.quantity, oi.unit_price " +
                "FROM order_items oi JOIN products p ON oi.product_id = p.product_id " +
                "WHERE oi.order_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(rs.getString("name") + " x" + rs.getInt("quantity") +
                        " @ Rs." + rs.getDouble("unit_price"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
