package ui;

import dao.ProductDAO;
import model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProductForm extends JFrame {

    private ProductDAO productDAO = new ProductDAO();
    private JTextField txtName, txtCategory, txtPrice, txtEcoRating, txtDescription;
    private JTable table;
    private DefaultTableModel tableModel;

    public ProductForm() {
        setTitle("GreenLoop - Product Catalogue");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));
        formPanel.setBackground(new Color(240, 248, 240));

        txtName        = new JTextField();
        txtCategory    = new JTextField();
        txtPrice       = new JTextField();
        txtEcoRating   = new JTextField();
        txtDescription = new JTextField();

        formPanel.add(new JLabel("  Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("  Category:"));
        formPanel.add(txtCategory);
        formPanel.add(new JLabel("  Price:"));
        formPanel.add(txtPrice);
        formPanel.add(new JLabel("  Eco Rating (1-5):"));
        formPanel.add(txtEcoRating);
        formPanel.add(new JLabel("  Description:"));
        formPanel.add(txtDescription);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(240, 248, 240));

        JButton btnAdd    = new JButton("Add Product");
        JButton btnUpdate = new JButton("Update Product");
        JButton btnDelete = new JButton("Delete Product");
        JButton btnClear  = new JButton("Clear");

        btnAdd.setBackground(new Color(34, 139, 34));
        btnAdd.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(178, 34, 34));
        btnDelete.setForeground(Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        String[] columns = {"ID", "Name", "Category", "Price", "Eco Rating", "Description"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setSelectionBackground(new Color(144, 238, 144));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Product List"));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadTable();

        btnAdd.addActionListener(e -> addProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnClear.addActionListener(e -> clearFields());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtCategory.setText(tableModel.getValueAt(row, 2).toString());
                txtPrice.setText(tableModel.getValueAt(row, 3).toString());
                txtEcoRating.setText(tableModel.getValueAt(row, 4).toString());
                txtDescription.setText(tableModel.getValueAt(row, 5).toString());
            }
        });

        setVisible(true);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                    p.getProductID(),
                    p.getName(),
                    p.getCategory(),
                    p.getPrice(),
                    p.getEcoRating(),
                    p.getDescription()
            });
        }
    }

    private void addProduct() {
        if (txtName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a product name!");
            return;
        }
        Product p = new Product(
                0,
                txtName.getText(),
                txtCategory.getText(),
                Double.parseDouble(txtPrice.getText()),
                Integer.parseInt(txtEcoRating.getText()),
                txtDescription.getText()
        );
        productDAO.addProduct(p);
        loadTable();
        clearFields();
        JOptionPane.showMessageDialog(this, "Product added successfully!");
    }

    private void updateProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to update!");
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        Product p = new Product(
                id,
                txtName.getText(),
                txtCategory.getText(),
                Double.parseDouble(txtPrice.getText()),
                Integer.parseInt(txtEcoRating.getText()),
                txtDescription.getText()
        );
        productDAO.updateProduct(p);
        loadTable();
        clearFields();
        JOptionPane.showMessageDialog(this, "Product updated successfully!");
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete!");
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this product?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            productDAO.deleteProduct(id);
            loadTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Product deleted!");
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtCategory.setText("");
        txtPrice.setText("");
        txtEcoRating.setText("");
        txtDescription.setText("");
        table.clearSelection();
    }
}