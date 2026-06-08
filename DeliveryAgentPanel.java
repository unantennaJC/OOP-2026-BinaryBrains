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
        setBackground(UITheme.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ---- Form panel ----
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(UITheme.WHITE);
        formPanel.setBorder(UITheme.getTitledBorder("Agent Details"));

        formPanel.add(makeLabel("Name:"));
        txtName = new JTextField(); formPanel.add(txtName);

        formPanel.add(makeLabel("Email:"));
        txtEmail = new JTextField(); formPanel.add(txtEmail);

        formPanel.add(makeLabel("Phone:"));
        txtPhone = new JTextField(); formPanel.add(txtPhone);

        formPanel.add(makeLabel("Vehicle Type:"));
        txtVehicleType = new JTextField(); formPanel.add(txtVehicleType);

        formPanel.add(makeLabel("Vehicle Plate:"));
        txtVehiclePlate = new JTextField(); formPanel.add(txtVehiclePlate);

        // ---- Buttons ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(UITheme.LIGHT_GRAY);

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        UITheme.stylePrimaryButton(btnAdd);
        UITheme.stylePrimaryButton(btnUpdate);
        UITheme.styleDangerButton(btnDelete);
        UITheme.styleSecondaryButton(btnClear);

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(UITheme.LIGHT_GRAY);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        // ---- Table ----
        String[] columns = {"ID", "Name", "Email", "Phone", "Vehicle Type", "Plate"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setBackground(UITheme.PRIMARY_GREEN);
        table.getTableHeader().setForeground(UITheme.WHITE);
        table.getTableHeader().setFont(UITheme.HEADER_FONT);
        table.setFont(UITheme.TABLE_FONT);
        table.setSelectionBackground(UITheme.ACCENT_GREEN);
        table.setSelectionForeground(UITheme.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.setBackground(UITheme.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

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

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UITheme.LABEL_FONT);
        label.setForeground(UITheme.TEXT_DARK);
        return label;
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