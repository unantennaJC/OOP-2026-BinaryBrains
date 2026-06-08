package com.greenloop.ui;

import com.greenloop.dao.DeliveryAgentDAO;
import com.greenloop.dao.DeliveryDAO;
import com.greenloop.dao.OrderDAO;
import com.greenloop.model.Delivery;
import com.greenloop.model.DeliveryAgent;
import com.greenloop.model.Order;
import com.greenloop.service.EmailService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DeliveryAssignmentPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private DeliveryDAO deliveryDAO = new DeliveryDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private DeliveryAgentDAO agentDAO = new DeliveryAgentDAO();
    private EmailService emailService = new EmailService();
    private int selectedDeliveryId = -1;

    public DeliveryAssignmentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // Assign panel
        JPanel assignPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        assignPanel.setBorder(BorderFactory.createTitledBorder("Assign Delivery Agent to Order"));

        assignPanel.add(new JLabel("Select Order:"));
        JComboBox<String> orderCombo = new JComboBox<>();
        loadOrders(orderCombo);
        assignPanel.add(orderCombo);

        assignPanel.add(new JLabel("Select Agent:"));
        JComboBox<String> agentCombo = new JComboBox<>();
        loadAgents(agentCombo);
        assignPanel.add(agentCombo);

        JButton btnAssign = new JButton("Assign & Send Emails");
        assignPanel.add(btnAssign);

        // Status update panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Update Delivery Status"));

        statusPanel.add(new JLabel("New Status:"));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{
                "Assigned", "In Transit", "Delivered", "Failed"
        });
        statusPanel.add(statusCombo);

        JButton btnUpdateStatus = new JButton("Update Status");
        statusPanel.add(btnUpdateStatus);

        JButton btnRefresh = new JButton("Refresh");
        statusPanel.add(btnRefresh);

        JPanel controlPanel = new JPanel(new GridLayout(2, 1));
        controlPanel.add(assignPanel);
        controlPanel.add(statusPanel);
        topPanel.add(controlPanel, BorderLayout.CENTER);

        // Table
        String[] columns = {"Delivery ID", "Order ID", "Client", "Agent", "Assigned Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadTable();

        // Assign button
        btnAssign.addActionListener(e -> {
            int orderIndex = orderCombo.getSelectedIndex();
            int agentIndex = agentCombo.getSelectedIndex();

            if (orderIndex < 0 || agentIndex < 0) {
                JOptionPane.showMessageDialog(this, "Please select both an order and an agent.");
                return;
            }

            List<Order> orders = orderDAO.getAllOrders();
            List<DeliveryAgent> agents = agentDAO.getAllAgents();

            Order selectedOrder = orders.get(orderIndex);
            DeliveryAgent selectedAgent = agents.get(agentIndex);

            if (deliveryDAO.assignAgent(selectedOrder.getOrderId(), selectedAgent.getAgentId())) {
                // Send emails
                String clientEmail = deliveryDAO.getClientEmail(selectedOrder.getOrderId());
                String clientName = deliveryDAO.getClientName(selectedOrder.getOrderId());
                String agentEmail = deliveryDAO.getAgentEmail(selectedAgent.getAgentId());

                if (clientEmail != null) {
                    emailService.sendDispatchEmail(clientEmail, clientName, String.valueOf(selectedOrder.getOrderId()));
                }
                if (agentEmail != null) {
                    emailService.sendAssignmentEmail(agentEmail, selectedAgent.getName(), String.valueOf(selectedOrder.getOrderId()));
                }

                JOptionPane.showMessageDialog(this,
                        "Agent assigned successfully!\nEmails sent to client and delivery agent.");
                loadTable();
                loadOrders(orderCombo);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to assign agent. Order may already have a delivery assigned.");
            }
        });

        // Update status button
        btnUpdateStatus.addActionListener(e -> {
            if (selectedDeliveryId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a delivery from the table first.");
                return;
            }
            String newStatus = (String) statusCombo.getSelectedItem();
            if (deliveryDAO.updateDeliveryStatus(selectedDeliveryId, newStatus)) {
                JOptionPane.showMessageDialog(this, "Delivery status updated to: " + newStatus);
                loadTable();
            }
        });

        btnRefresh.addActionListener(e -> {
            loadTable();
            loadOrders(orderCombo);
            loadAgents(agentCombo);
        });

        // Row click
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedDeliveryId = (int) tableModel.getValueAt(row, 0);
            }
        });
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Delivery> deliveries = deliveryDAO.getAllDeliveries();
        for (Delivery d : deliveries) {
            tableModel.addRow(new Object[]{
                    d.getDeliveryId(), d.getOrderId(), d.getClientName(),
                    d.getAgentName(), d.getAssignedDate(), d.getDeliveryStatus()
            });
        }
    }

    private void loadOrders(JComboBox<String> combo) {
        combo.removeAllItems();
        List<Order> orders = orderDAO.getAllOrders();
        for (Order o : orders) {
            if (o.getStatus().equals("Pending")) {
                combo.addItem("Order #" + o.getOrderId() + " - " + o.getClientName());
            }
        }
    }

    private void loadAgents(JComboBox<String> combo) {
        combo.removeAllItems();
        List<DeliveryAgent> agents = agentDAO.getAllAgents();
        for (DeliveryAgent a : agents) {
            combo.addItem(a.getName() + " (" + a.getVehicleType() + ")");
        }
    }
}
