package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private Connection conn;

    public ClientDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    // Add a new client to database
    public void addClient(Client c) {
        String sql = "INSERT INTO clients (id, name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, c.getId());
            stmt.setString(2, c.getName());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getPhone());
            stmt.setString(5, c.getAddress());
            stmt.executeUpdate();
            System.out.println("Client added to database!");
        } catch (SQLException e) {
            System.out.println("Error adding client: " + e.getMessage());
        }
    }

    // Get all clients from database
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                clients.add(new Client(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error getting clients: " + e.getMessage());
        }
        return clients;
    }

    // Update client in database
    public void updateClient(Client c) {
        String sql = "UPDATE clients SET name=?, email=?, phone=?, address=? WHERE id=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, c.getName());
            stmt.setString(2, c.getEmail());
            stmt.setString(3, c.getPhone());
            stmt.setString(4, c.getAddress());
            stmt.setString(5, c.getId());
            stmt.executeUpdate();
            System.out.println("Client updated in database!");
        } catch (SQLException e) {
            System.out.println("Error updating client: " + e.getMessage());
        }
    }

    // Delete client from database
    public void deleteClient(String id) {
        String sql = "DELETE FROM clients WHERE id=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.executeUpdate();
            System.out.println("Client deleted from database!");
        } catch (SQLException e) {
            System.out.println("Error deleting client: " + e.getMessage());
        }
    }

    // Search clients by name
    public List<Client> searchClients(String keyword) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE name LIKE ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                clients.add(new Client(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error searching clients: " + e.getMessage());
        }
        return clients;
    }

    // Get total number of clients
    public int getTotalClients() {
        String sql = "SELECT COUNT(*) FROM clients";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error counting clients: " + e.getMessage());
        }
        return 0;
    }
}