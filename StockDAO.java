package dao;

import model.Stock;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {
    private Connection conn;

    public StockDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    public void addStock(Stock s) {
        String sql = "INSERT INTO stock (productId, quantityOnHand, reorderLevel) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, s.getProductID());
            stmt.setInt(2, s.getQuantityOnHand());
            stmt.setInt(3, s.getReorderLevel());
            stmt.executeUpdate();
            System.out.println("Stock added!");
        } catch (SQLException e) {
            System.out.println("Error adding stock: " + e.getMessage());
        }
    }

    public List<Stock> getAllStock() {
        List<Stock> stockList = new ArrayList<>();
        String sql = "SELECT * FROM stock";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                stockList.add(new Stock(
                        rs.getInt("stockId"),
                        rs.getInt("productId"),
                        rs.getInt("quantityOnHand"),
                        rs.getInt("reorderLevel")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error getting stock: " + e.getMessage());
        }
        return stockList;
    }

    public void updateStock(Stock s) {
        String sql = "UPDATE stock SET quantityOnHand = ?, reorderLevel = ? WHERE stockId = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, s.getQuantityOnHand());
            stmt.setInt(2, s.getReorderLevel());
            stmt.setInt(3, s.getStockID());
            stmt.executeUpdate();
            System.out.println("Stock updated!");
        } catch (SQLException e) {
            System.out.println("Error updating stock: " + e.getMessage());
        }
    }

    public void deleteStock(int stockId) {
        String sql = "DELETE FROM stock WHERE stockId = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, stockId);
            stmt.executeUpdate();
            System.out.println("Stock deleted!");
        } catch (SQLException e) {
            System.out.println("Error deleting stock: " + e.getMessage());
        }
    }

    public List<Stock> getLowStock() {
        List<Stock> lowStock = new ArrayList<>();
        String sql = "SELECT * FROM stock WHERE quantityOnHand <= reorderLevel";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lowStock.add(new Stock(
                        rs.getInt("stockId"),
                        rs.getInt("productId"),
                        rs.getInt("quantityOnHand"),
                        rs.getInt("reorderLevel")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error getting low stock: " + e.getMessage());
        }
        return lowStock;
    }
}