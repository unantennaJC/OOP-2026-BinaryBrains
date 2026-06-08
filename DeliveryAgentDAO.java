package com.greenloop.dao;

import com.greenloop.model.DeliveryAgent;
import com.greenloop.database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryAgentDAO {

    // Get all agents from database
    public List<DeliveryAgent> getAllAgents() {
        List<DeliveryAgent> list = new ArrayList<>();
        String sql = "SELECT * FROM delivery_agents";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new DeliveryAgent(
                        rs.getInt("agent_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("vehicle_type"),
                        rs.getString("vehicle_plate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Add a new agent
    public boolean addAgent(DeliveryAgent agent) {
        String sql = "INSERT INTO delivery_agents (name, email, phone, vehicle_type, vehicle_plate) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, agent.getName());
            ps.setString(2, agent.getEmail());
            ps.setString(3, agent.getPhone());
            ps.setString(4, agent.getVehicleType());
            ps.setString(5, agent.getVehiclePlate());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update an existing agent
    public boolean updateAgent(DeliveryAgent agent) {
        String sql = "UPDATE delivery_agents SET name=?, email=?, phone=?, vehicle_type=?, vehicle_plate=? WHERE agent_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, agent.getName());
            ps.setString(2, agent.getEmail());
            ps.setString(3, agent.getPhone());
            ps.setString(4, agent.getVehicleType());
            ps.setString(5, agent.getVehiclePlate());
            ps.setInt(6, agent.getAgentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete an agent
    public boolean deleteAgent(int agentId) {
        String sql = "DELETE FROM delivery_agents WHERE agent_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
