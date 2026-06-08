package com.greenloop.dao;

import com.greenloop.database.DBConnection;
import com.greenloop.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public List<Client> getAllClients() {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Client(
                        rs.getInt("client_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addClient(Client client) {
        String sql = "INSERT INTO clients (name, email, phone, address) VALUES (?,?,?,?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, client.getName());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getPhone());
            ps.setString(4, client.getAddress());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateClient(Client client) {
        String sql = "UPDATE clients SET name=?, email=?, phone=?, address=? WHERE client_id=?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, client.getName());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getPhone());
            ps.setString(4, client.getAddress());
            ps.setInt(5, client.getClientId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteClient(int clientId) {
        String sql = "DELETE FROM clients WHERE client_id=?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, clientId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
