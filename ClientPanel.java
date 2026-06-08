package com.greenloop.ui;

import com.greenloop.dao.ClientDAO;
import com.greenloop.model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientPanel extends JPanel {

    private JTextField txtName, txtEmail, txtPhone, txtAddress;
    private JTable table;
    private DefaultTableModel tableModel;
    private ClientDAO dao = new ClientDAO();
    private int selectedClientId = -1;

    public ClientPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Form panel ----
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Client Details"));

        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField(); formPanel.add(txtName);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(); formPanel.add(txtEmail);

        formPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField(); formPanel.add(txtPhone);

        formPanel.add(new JLabel("Address:"));
        txtAddress = new JTextField(); formPanel.add(txtAddress);

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
        String[] columns = {"ID", "Name", "Email", "Phone", "Address"};
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
            Client client = new Client(0,
                    txtName.getText(),
                    txtEmail.getText(),
                    txtPhone.getText(),
                    txtAddress.getText());
            if (dao.addClient(client)) {
                JOptionPane.showMessageDialog(this, "Client added successfully!");
                loadTable(); clearForm();
            }
        });

        btnUpdate.addActionListener(e -> {
            if (selectedClientId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a client from the table first.");
                return;
            }
            Client client = new Client(selectedClientId,
                    txtName.getText(),
                    txtEmail.getText(),
                    txtPhone.getText(),
                    txtAddress.getText());
            if (dao.updateClient(client)) {
                JOptionPane.showMessageDialog(this, "Client updated successfully!");
                loadTable(); clearForm();
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedClientId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a client from the table first.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this client?");
            if (confirm == JOptionPane.YES_OPTION) {
                dao.deleteClient(selectedClientId);
                JOptionPane.showMessageDialog(this, "Client deleted.");
                loadTable(); clearForm();
            }
        });

        btnClear.addActionListener(e -> clearForm());

        // ---- Row click fills form ----
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedClientId = (int) tableModel.getValueAt(row, 0);
                txtName.setText((String) tableModel.getValueAt(row, 1));
                txtEmail.setText((String) tableModel.getValueAt(row, 2));
                txtPhone.setText((String) tableModel.getValueAt(row, 3));
                txtAddress.setText((String) tableModel.getValueAt(row, 4));
            }
        });
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Client> clients = dao.getAllClients();
        for (Client c : clients) {
            tableModel.addRow(new Object[]{
                    c.getClientId(), c.getName(), c.getEmail(),
                    c.getPhone(), c.getAddress()
            });
        }
    }

    private void clearForm() {
        txtName.setText(""); txtEmail.setText("");
        txtPhone.setText(""); txtAddress.setText("");
        selectedClientId = -1;
        table.clearSelection();
    }
}