package com.greenloop.dao;

import com.greenloop.database.DBConnection;
import com.greenloop.model.Delivery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDAO {

    // Get all deliveries
    public List<Delivery> getAllDeliveries() {
        List<Delivery> list = new ArrayList<>();
        String sql = "SELECT d.delivery_id, d.order_id, d.agent_id, " +
                "da.name as agent_name, c.name as client_name, " +
                "d.assigned_date, d.delivery_status " +
                "FROM deliveries d " +
                "JOIN delivery_agents da ON d.agent_id = da.agent_id " +
                "JOIN orders o ON d.order_id = o.order_id " +
                "JOIN clients c ON o.client_id = c.client_id";
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Delivery(
                        rs.getInt("delivery_id"),
                        rs.getInt("order_id"),
                        rs.getInt("agent_id"),
                        rs.getString("agent_name"),
                        rs.getString("client_name"),
                        rs.getString("assigned_date"),
                        rs.getString("delivery_status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Assign delivery agent to order
    public boolean assignAgent(int orderId, int agentId) {
        String sql = "INSERT INTO deliveries (order_id, agent_id, assigned_date, delivery_status) " +
                "VALUES (?, ?, CURDATE(), 'Assigned')";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ps.setInt(2, agentId);
            ps.executeUpdate();

            // Update order status to Dispatched
            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE orders SET status = 'Dispatched' WHERE order_id = ?");
            ps2.setInt(1, orderId);
            ps2.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update delivery status
    public boolean updateDeliveryStatus(int deliveryId, String status) {
        String sql = "UPDATE deliveries SET delivery_status = ? WHERE delivery_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, deliveryId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get client email for an order
    public String getClientEmail(int orderId) {
        String sql = "SELECT c.email FROM clients c " +
                "JOIN orders o ON c.client_id = o.client_id " +
                "WHERE o.order_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("email");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get client name for an order
    public String getClientName(int orderId) {
        String sql = "SELECT c.name FROM clients c " +
                "JOIN orders o ON c.client_id = o.client_id " +
                "WHERE o.order_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get agent email
    public String getAgentEmail(int agentId) {
        String sql = "SELECT email FROM delivery_agents WHERE agent_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, agentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("email");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
