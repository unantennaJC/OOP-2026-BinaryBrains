package ui;

import dao.StockDAO;
import model.Stock;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class StockForm extends JFrame {

    private StockDAO stockDAO = new StockDAO();
    private JTextField txtProductId, txtQuantity, txtReorderLevel;
    private JTable table;
    private DefaultTableModel tableModel;

    public StockForm() {
        setTitle("GreenLoop - Stock Management");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Stock Details"));
        formPanel.setBackground(new Color(240, 248, 240));

        txtProductId    = new JTextField();
        txtQuantity     = new JTextField();
        txtReorderLevel = new JTextField();

        formPanel.add(new JLabel("  Product ID:"));
        formPanel.add(txtProductId);
        formPanel.add(new JLabel("  Quantity on Hand:"));
        formPanel.add(txtQuantity);
        formPanel.add(new JLabel("  Reorder Level:"));
        formPanel.add(txtReorderLevel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(240, 248, 240));

        JButton btnAdd      = new JButton("Add Stock");
        JButton btnUpdate   = new JButton("Update Stock");
        JButton btnDelete   = new JButton("Delete Stock");
        JButton btnLowStock = new JButton("Show Low Stock");
        JButton btnClear    = new JButton("Clear");

        btnAdd.setBackground(new Color(34, 139, 34));
        btnAdd.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(178, 34, 34));
        btnDelete.setForeground(Color.WHITE);
        btnLowStock.setBackground(new Color(255, 140, 0));
        btnLowStock.setForeground(Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnLowStock);
        buttonPanel.add(btnClear);

        String[] columns = {"Stock ID", "Product ID", "Quantity", "Reorder Level", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setSelectionBackground(new Color(144, 238, 144));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Stock List"));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadTable();

        btnAdd.addActionListener(e -> addStock());
        btnUpdate.addActionListener(e -> updateStock());
        btnDelete.addActionListener(e -> deleteStock());
        btnLowStock.addActionListener(e -> showLowStock());
        btnClear.addActionListener(e -> clearFields());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtProductId.setText(tableModel.getValueAt(row, 1).toString());
                txtQuantity.setText(tableModel.getValueAt(row, 2).toString());
                txtReorderLevel.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        setVisible(true);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Stock> stockList = stockDAO.getAllStock();
        for (Stock s : stockList) {
            String status = s.isLowStock() ? "LOW STOCK" : "OK";
            tableModel.addRow(new Object[]{
                    s.getStockID(),
                    s.getProductID(),
                    s.getQuantityOnHand(),
                    s.getReorderLevel(),
                    status
            });
        }
    }

    private void addStock() {
        if (txtProductId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Product ID!");
            return;
        }
        Stock s = new Stock(
                0,
                Integer.parseInt(txtProductId.getText()),
                Integer.parseInt(txtQuantity.getText()),
                Integer.parseInt(txtReorderLevel.getText())
        );
        stockDAO.addStock(s);
        loadTable();
        clearFields();
        JOptionPane.showMessageDialog(this, "Stock added successfully!");
    }

    private void updateStock() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a stock item to update!");
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        Stock s = new Stock(
                id,
                Integer.parseInt(txtProductId.getText()),
                Integer.parseInt(txtQuantity.getText()),
                Integer.parseInt(txtReorderLevel.getText())
        );
        stockDAO.updateStock(s);
        loadTable();
        clearFields();
        JOptionPane.showMessageDialog(this, "Stock updated successfully!");
    }

    private void deleteStock() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a stock item to delete!");
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this stock item?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            stockDAO.deleteStock(id);
            loadTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Stock deleted!");
        }
    }

    private void showLowStock() {
        tableModel.setRowCount(0);
        List<Stock> lowStock = stockDAO.getLowStock();
        if (lowStock.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No low stock items!");
        } else {
            for (Stock s : lowStock) {
                tableModel.addRow(new Object[]{
                        s.getStockID(),
                        s.getProductID(),
                        s.getQuantityOnHand(),
                        s.getReorderLevel(),
                        "LOW STOCK"
                });
            }
        }
    }

    private void clearFields() {
        txtProductId.setText("");
        txtQuantity.setText("");
        txtReorderLevel.setText("");
        table.clearSelection();
    }
}
