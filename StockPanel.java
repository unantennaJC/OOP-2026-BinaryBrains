package com.greenloop.ui;

import com.greenloop.dao.StockDAO;
import com.greenloop.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StockPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private StockDAO dao = new StockDAO();
    private int selectedProductId = -1;

    public StockPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Top controls ----
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // Stock In panel
        JPanel stockInPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stockInPanel.setBorder(BorderFactory.createTitledBorder("Stock In from Supplier"));

        stockInPanel.add(new JLabel("Quantity to Add:"));
        JTextField txtQuantity = new JTextField(8);
        stockInPanel.add(txtQuantity);

        JButton btnStockIn = new JButton("Stock In");
        stockInPanel.add(btnStockIn);

        // Reorder level panel
        JPanel reorderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reorderPanel.setBorder(BorderFactory.createTitledBorder("Update Reorder Level"));

        reorderPanel.add(new JLabel("New Reorder Level:"));
        JTextField txtReorderLevel = new JTextField(8);
        reorderPanel.add(txtReorderLevel);

        JButton btnUpdateReorder = new JButton("Update");
        reorderPanel.add(btnUpdateReorder);

        // Filter buttons
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnShowAll = new JButton("Show All Stock");
        JButton btnLowStock = new JButton("Show Low Stock Alerts");
        filterPanel.add(btnShowAll);
        filterPanel.add(btnLowStock);

        JPanel controlPanel = new JPanel(new GridLayout(3, 1));
        controlPanel.add(stockInPanel);
        controlPanel.add(reorderPanel);
        controlPanel.add(filterPanel);

        topPanel.add(controlPanel, BorderLayout.CENTER);

        // ---- Table ----
        String[] columns = {"ID", "Name", "Description", "Price", "Eco Rating", "Quantity on Hand", "Reorder Level", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // ---- Load data ----
        loadTable(false);

        // ---- Button actions ----
        btnStockIn.addActionListener(e -> {
            if (selectedProductId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product from the table first.");
                return;
            }
            try {
                int quantity = Integer.parseInt(txtQuantity.getText());
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
                    return;
                }
                if (dao.stockIn(selectedProductId, quantity)) {
                    JOptionPane.showMessageDialog(this, "Stock updated successfully!");
                    txtQuantity.setText("");
                    loadTable(false);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for quantity.");
            }
        });

        btnUpdateReorder.addActionListener(e -> {
            if (selectedProductId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product from the table first.");
                return;
            }
            try {
                int reorderLevel = Integer.parseInt(txtReorderLevel.getText());
                if (dao.updateReorderLevel(selectedProductId, reorderLevel)) {
                    JOptionPane.showMessageDialog(this, "Reorder level updated successfully!");
                    txtReorderLevel.setText("");
                    loadTable(false);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for reorder level.");
            }
        });

        btnShowAll.addActionListener(e -> loadTable(false));

        btnLowStock.addActionListener(e -> loadTable(true));

        // ---- Row click ----
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedProductId = (int) tableModel.getValueAt(row, 0);
            }
        });
    }

    private void loadTable(boolean lowStockOnly) {
        tableModel.setRowCount(0);
        List<Product> products = lowStockOnly ? dao.getLowStockProducts() : dao.getAllStock();
        for (Product p : products) {
            String status = p.getQuantityOnHand() <= p.getReorderLevel() ? "⚠ Low Stock" : "OK";
            tableModel.addRow(new Object[]{
                    p.getProductId(), p.getName(), p.getDescription(),
                    p.getPrice(), p.getEcoRating(),
                    p.getQuantityOnHand(), p.getReorderLevel(), status
            });
        }
    }
}