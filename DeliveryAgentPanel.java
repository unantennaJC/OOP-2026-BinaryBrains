package com.greenloop.ui;

import com.greenloop.dao.DeliveryAgentDAO;
import com.greenloop.model.DeliveryAgent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DeliveryAgentPanel extends JPanel {

    private JTextField txtName, txtEmail, txtPhone, txtVehicleType, txtVehiclePlate;
    private JTable table;
    private DefaultTableModel tableModel;
    private DeliveryAgentDAO dao = new DeliveryAgentDAO();
    private int selectedAgentId = -1;

    public DeliveryAgentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Form panel ----
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Agent Details"));

        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField(); formPanel.add(txtName);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(); formPanel.add(txtEmail);

        formPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField(); formPanel.add(txtPhone);

        formPanel.add(new JLabel("Vehicle Type:"));
        txtVehicleType = new JTextField(); formPanel.add(txtVehicleType);

        formPanel.add(new JLabel("Vehicle Plate:"));
        txtVehiclePlate = new JTextField(); formPanel.add(txtVehiclePlate);

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
        String[] columns = {"ID", "Name", "Email", "Phone", "Vehicle Type", "Plate"};
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
            if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Email are required.");
                return;
            }
            DeliveryAgent agent = new DeliveryAgent(0,
                    txtName.getText(), txtEmail.getText(),
                    txtPhone.getText(), txtVehicleType.getText(), txtVehiclePlate.getText());
            if (dao.addAgent(agent)) {
                JOptionPane.showMessageDialog(this, "Agent added successfully!");
                loadTable(); clearForm();
            }
        });

        btnUpdate.addActionListener(e -> {
            if (selectedAgentId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an agent from the table first.");
                return;
            }
            DeliveryAgent agent = new DeliveryAgent(selectedAgentId,
                    txtName.getText(), txtEmail.getText(),
                    txtPhone.getText(), txtVehicleType.getText(), txtVehiclePlate.getText());
            if (dao.updateAgent(agent)) {
                JOptionPane.showMessageDialog(this, "Agent updated successfully!");
                loadTable(); clearForm();
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedAgentId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an agent from the table first.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this agent?");
            if (confirm == JOptionPane.YES_OPTION) {
                dao.deleteAgent(selectedAgentId);
                JOptionPane.showMessageDialog(this, "Agent deleted.");
                loadTable(); clearForm();
            }
        });

        btnClear.addActionListener(e -> clearForm());

        // ---- Row click fills form ----
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedAgentId = (int) tableModel.getValueAt(row, 0);
                txtName.setText((String) tableModel.getValueAt(row, 1));
                txtEmail.setText((String) tableModel.getValueAt(row, 2));
                txtPhone.setText((String) tableModel.getValueAt(row, 3));
                txtVehicleType.setText((String) tableModel.getValueAt(row, 4));
                txtVehiclePlate.setText((String) tableModel.getValueAt(row, 5));
            }
        });
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<DeliveryAgent> agents = dao.getAllAgents();
        for (DeliveryAgent a : agents) {
            tableModel.addRow(new Object[]{
                    a.getAgentId(), a.getName(), a.getEmail(),
                    a.getPhone(), a.getVehicleType(), a.getVehiclePlate()
            });
        }
    }

    private void clearForm() {
        txtName.setText(""); txtEmail.setText("");
        txtPhone.setText(""); txtVehicleType.setText("");
        txtVehiclePlate.setText(""); selectedAgentId = -1;
        table.clearSelection();
    }
}