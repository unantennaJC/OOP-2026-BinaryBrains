package dao;

import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection conn;

    public ProductDAO() {
        this.conn = DatabaseConnection.getConnection();
    }

    public void addProduct(Product p) {
        String sql = "INSERT INTO products (name, category, price, ecoRating, description) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getCategory());
            stmt.setDouble(3, p.getPrice());
            stmt.setInt(4, p.getEcoRating());
            stmt.setString(5, p.getDescription());
            stmt.executeUpdate();
            System.out.println("Product added!");
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("productId"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("ecoRating"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error getting products: " + e.getMessage());
        }
        return products;
    }

    public void updateProduct(Product p) {
        String sql = "UPDATE products SET name=?, category=?, price=?, ecoRating=?, description=? WHERE productId=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getCategory());
            stmt.setDouble(3, p.getPrice());
            stmt.setInt(4, p.getEcoRating());
            stmt.setString(5, p.getDescription());
            stmt.setInt(6, p.getProductID());
            stmt.executeUpdate();
            System.out.println("Product updated!");
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public void deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE productId=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            stmt.executeUpdate();
            System.out.println("Product deleted!");
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }
}