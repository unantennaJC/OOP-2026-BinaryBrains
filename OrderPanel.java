package com.greenloop.ui;

import com.greenloop.dao.ClientDAO;
import com.greenloop.dao.OrderDAO;
import com.greenloop.dao.ProductDAO;
import com.greenloop.model.Client;
import com.greenloop.model.Order;
import com.greenloop.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderPanel extends JPanel {

    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private OrderDAO orderDAO = new OrderDAO();
    private ClientDAO clientDAO = new ClientDAO();
    private ProductDAO productDAO = new ProductDAO();
    private int selectedOrderId = -1;

    public OrderPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // Create order panel
        JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        createPanel.setBorder(BorderFactory.createTitledBorder("Create New Order"));
        createPanel.add(new JLabel("Select Client:"));
        JComboBox<String> clientCombo = new JComboBox<>();
        loadClients(clientCombo);
        createPanel.add(clientCombo);
        JButton btnCreateOrder = new JButton("Create Order");
        createPanel.add(btnCreateOrder);

        // Add product panel
        JPanel addProductPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addProductPanel.setBorder(BorderFactory.createTitledBorder("Add Product to Selected Order"));
        addProductPanel.add(new JLabel("Product:"));
        JComboBox<String> productCombo = new JComboBox<>();
        loadProducts(productCombo);
        addProductPanel.add(productCombo);
        addProductPanel.add(new JLabel("Quantity:"));
        JTextField txtQuantity = new JTextField(5);
        addProductPanel.add(txtQuantity);
        JButton btnAddProduct = new JButton("Add Product");
        addProductPanel.add(btnAddProduct);

        // Buttons panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnViewItems = new JButton("View Order Items");
        JButton btnDeleteOrder = new JButton("Delete Order");
        JButton btnRefresh = new JButton("Refresh");
        btnPanel.add(btnViewItems);
        btnPanel.add(btnDeleteOrder);
        btnPanel.add(btnRefresh);

        JPanel controlPanel = new JPanel(new GridLayout(3, 1));
        controlPanel.add(createPanel);
        controlPanel.add(addProductPanel);
        controlPanel.add(btnPanel);
        topPanel.add(controlPanel, BorderLayout.CENTER);

        // Table
        String[] columns = {"Order ID", "Client", "Order Date", "Status", "Total (Rs.)"};
        orderTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        orderTable = new JTable(orderTableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadOrders();

        // Button actions
        btnCreateOrder.addActionListener(e -> {
            int selectedIndex = clientCombo.getSelectedIndex();
            if (selectedIndex < 0) {
                JOptionPane.showMessageDialog(this, "Please select a client.");
                return;
            }
            List<Client> clients = clientDAO.getAllClients();
            int clientId = clients.get(selectedIndex).getClientId();
            int orderId = orderDAO.createOrder(clientId);
            if (orderId != -1) {
                JOptionPane.showMessageDialog(this, "Order #" + orderId + " created successfully!");
                loadOrders();
            }
        });

        btnAddProduct.addActionListener(e -> {
            if (selectedOrderId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order from the table first.");
                return;
            }
            int selectedIndex = productCombo.getSelectedIndex();
            if (selectedIndex < 0) {
                JOptionPane.showMessageDialog(this, "Please select a product.");
                return;
            }
            try {
                int quantity = Integer.parseInt(txtQuantity.getText());
                List<Product> products = productDAO.getAllProducts();
                Product selectedProduct = products.get(selectedIndex);
                if (quantity > selectedProduct.getQuantityOnHand()) {
                    JOptionPane.showMessageDialog(this, "Not enough stock! Available: " + selectedProduct.getQuantityOnHand());
                    return;
                }
                if (orderDAO.addOrderItem(selectedOrderId, selectedProduct.getProductId(), quantity, selectedProduct.getPrice())) {
                    JOptionPane.showMessageDialog(this, "Product added to order successfully!");
                    txtQuantity.setText("");
                    loadOrders();
                    loadProducts(productCombo);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
            }
        });

        btnViewItems.addActionListener(e -> {
            if (selectedOrderId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order first.");
                return;
            }
            List<String> items = orderDAO.getOrderItems(selectedOrderId);
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No items in this order yet.");
            } else {
                StringBuilder sb = new StringBuilder("Order #" + selectedOrderId + " Items:\n\n");
                for (String item : items) {
                    sb.append("• ").append(item).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString());
            }
        });

        btnDeleteOrder.addActionListener(e -> {
            if (selectedOrderId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order first.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Order #" + selectedOrderId + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                orderDAO.deleteOrder(selectedOrderId);
                JOptionPane.showMessageDialog(this, "Order deleted.");
                loadOrders();
                selectedOrderId = -1;
            }
        });

        btnRefresh.addActionListener(e -> {
            loadOrders();
            loadClients(clientCombo);
            loadProducts(productCombo);
        });

        orderTable.getSelectionModel().addListSelectionListener(e -> {
            int row = orderTable.getSelectedRow();
            if (row >= 0) {
                selectedOrderId = (int) orderTableModel.getValueAt(row, 0);
            }
        });
    }

    private void loadOrders() {
        orderTableModel.setRowCount(0);
        List<Order> orders = orderDAO.getAllOrders();
        for (Order o : orders) {
            orderTableModel.addRow(new Object[]{
                    o.getOrderId(), o.getClientName(),
                    o.getOrderDate(), o.getStatus(), o.getTotalAmount()
            });
        }
    }

    private void loadClients(JComboBox<String> combo) {
        combo.removeAllItems();
        List<Client> clients = clientDAO.getAllClients();
        for (Client c : clients) {
            combo.addItem(c.getName());
        }
    }

    private void loadProducts(JComboBox<String> combo) {
        combo.removeAllItems();
        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            combo.addItem(p.getName() + " (Stock: " + p.getQuantityOnHand() + " | Rs." + p.getPrice() + ")");
        }
    }
}