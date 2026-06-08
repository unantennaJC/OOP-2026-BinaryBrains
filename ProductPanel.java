package com.greenloop.ui;

import com.greenloop.dao.ProductDAO;
import com.greenloop.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JPanel {

    private JTextField txtName, txtDescription, txtPrice, txtEcoRating, txtQuantity, txtReorderLevel;
    private JTable table;
    private DefaultTableModel tableModel;
    private ProductDAO dao = new ProductDAO();
    private int selectedProductId = -1;

    public ProductPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Form panel ----
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));

        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField(); formPanel.add(txtName);

        formPanel.add(new JLabel("Description:"));
        txtDescription = new JTextField(); formPanel.add(txtDescription);

        formPanel.add(new JLabel("Price:"));
        txtPrice = new JTextField(); formPanel.add(txtPrice);

        formPanel.add(new JLabel("Eco Rating (1-5):"));
        txtEcoRating = new JTextField(); formPanel.add(txtEcoRating);

        formPanel.add(new JLabel("Quantity on Hand:"));
        txtQuantity = new JTextField(); formPanel.add(txtQuantity);

        formPanel.add(new JLabel("Reorder Level:"));
        txtReorderLevel = new JTextField(); formPanel.add(txtReorderLevel);

        // ---- Buttons ----
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        // ---- Table ----
        String[] columns = {"ID", "Name", "Description", "Price", "Eco Rating", "Quantity", "Reorder Level"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // ---- Load data ----
        loadTable();

        // ---- Button actions ----
        btnAdd.addActionListener(e -> {
            if (txtName.getText().isEmpty() || txtPrice.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Price are required.");
                return;
            }
            try {
                Product product = new Product(0,
                        txtName.getText(),
                        txtDescription.getText(),
                        Double.parseDouble(txtPrice.getText()),
                        Integer.parseInt(txtEcoRating.getText()),
                        Integer.parseInt(txtQuantity.getText()),
                        Integer.parseInt(txtReorderLevel.getText()));
                if (dao.addProduct(product)) {
                    JOptionPane.showMessageDialog(this, "Product added successfully!");
                    loadTable(); clearForm();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for Price, Eco Rating, Quantity and Reorder Level.");
            }
        });

        btnUpdate.addActionListener(e -> {
            if (selectedProductId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product from the table first.");
                return;
            }
            try {
                Product product = new Product(selectedProductId,
                        txtName.getText(),
                        txtDescription.getText(),
                        Double.parseDouble(txtPrice.getText()),
                        Integer.parseInt(txtEcoRating.getText()),
                        Integer.parseInt(txtQuantity.getText()),
                        Integer.parseInt(txtReorderLevel.getText()));
                if (dao.updateProduct(product)) {
                    JOptionPane.showMessageDialog(this, "Product updated successfully!");
                    loadTable(); clearForm();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for Price, Eco Rating, Quantity and Reorder Level.");
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedProductId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product from the table first.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?");
            if (confirm == JOptionPane.YES_OPTION) {
                dao.deleteProduct(selectedProductId);
                JOptionPane.showMessageDialog(this, "Product deleted.");
                loadTable(); clearForm();
            }
        });

        btnClear.addActionListener(e -> clearForm());

        // ---- Row click fills form ----
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedProductId = (int) tableModel.getValueAt(row, 0);
                txtName.setText((String) tableModel.getValueAt(row, 1));
                txtDescription.setText((String) tableModel.getValueAt(row, 2));
                txtPrice.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                txtEcoRating.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                txtQuantity.setText(String.valueOf(tableModel.getValueAt(row, 5)));
                txtReorderLevel.setText(String.valueOf(tableModel.getValueAt(row, 6)));
            }
        });
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Product> products = dao.getAllProducts();
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                    p.getProductId(), p.getName(), p.getDescription(),
                    p.getPrice(), p.getEcoRating(), p.getQuantityOnHand(), p.getReorderLevel()
            });
        }
    }

    private void clearForm() {
        txtName.setText(""); txtDescription.setText("");
        txtPrice.setText(""); txtEcoRating.setText("");
        txtQuantity.setText(""); txtReorderLevel.setText("");
        selectedProductId = -1;
        table.clearSelection();
    }
}
