// ClientPanel.java - Task 2: Manage Clients
// This is the screen the user sees to manage clients

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ClientPanel extends JPanel {

    private ClientManager clientManager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    // Form fields
    private JTextField nameField, emailField, phoneField, addressField;
    private JButton addBtn, updateBtn, deleteBtn, clearBtn;

    public ClientPanel(ClientManager clientManager) {
        this.clientManager = clientManager;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 250, 245));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildFormPanel(), BorderLayout.EAST);

        refreshTable();
    }

    // Top search bar
    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(34, 139, 34));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JLabel title = new JLabel("👥 Manage Clients - GreenLoop");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JButton searchBtn = new JButton("🔍 Search");
        searchBtn.setBackground(Color.WHITE);
        searchBtn.addActionListener(e -> searchClients());

        JButton showAllBtn = new JButton("Show All");
        showAllBtn.setBackground(Color.WHITE);
        showAllBtn.addActionListener(e -> { searchField.setText(""); refreshTable(); });

        panel.add(title);
        panel.add(Box.createHorizontalStrut(30));
        panel.add(new JLabel("Search:") {{ setForeground(Color.WHITE); }});
        panel.add(searchField);
        panel.add(searchBtn);
        panel.add(showAllBtn);
        return panel;
    }

    // Client list table
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Client List"));
        panel.setBackground(Color.WHITE);

        String[] columns = {"ID", "Name", "Email", "Phone", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(34, 139, 34));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(144, 238, 144));

        // When user clicks a row, fill the form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromTable();
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // Add/Update/Delete form on the right
    private JPanel buildFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Client Details"));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(280, 0));

        nameField    = createField(panel, "Name:");
        emailField   = createField(panel, "Email:");
        phoneField   = createField(panel, "Phone:");
        addressField = createField(panel, "Address:");

        panel.add(Box.createVerticalStrut(15));

        addBtn    = createButton("➕ Add Client",    new Color(34, 139, 34));
        updateBtn = createButton("✏️ Update Client", new Color(70, 130, 180));
        deleteBtn = createButton("🗑️ Delete Client", new Color(220, 50, 50));
        clearBtn  = createButton("🔄 Clear Form",    new Color(128, 128, 128));

        addBtn.addActionListener(e -> addClient());
        updateBtn.addActionListener(e -> updateClient());
        deleteBtn.addActionListener(e -> deleteClient());
        clearBtn.addActionListener(e -> clearForm());

        panel.add(addBtn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(updateBtn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(deleteBtn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(clearBtn);

        return panel;
    }

    private JTextField createField(JPanel panel, String label) {
        panel.add(new JLabel(label) {{ setFont(new Font("Segoe UI", Font.BOLD, 12)); setAlignmentX(LEFT_ALIGNMENT); }});
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(field);
        panel.add(Box.createVerticalStrut(8));
        return field;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setFocusPainted(false);
        return btn;
    }

    // ---- Actions ----

    private void addClient() {
        String name    = nameField.getText().trim();
        String email   = emailField.getText().trim();
        String phone   = phoneField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Phone are required!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        clientManager.addClient(name, email, phone, address);
        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this, "✅ Client added successfully!");
    }

    private void updateClient() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a client first!"); return; }

        String id = (String) tableModel.getValueAt(row, 0);
        Client selected = getClientById(id);
        if (selected == null) return;

        clientManager.updateClient(selected,
            nameField.getText().trim(),
            emailField.getText().trim(),
            phoneField.getText().trim(),
            addressField.getText().trim());
        refreshTable();
        JOptionPane.showMessageDialog(this, "✅ Client updated successfully!");
    }

    private void deleteClient() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a client first!"); return; }

        String id = (String) tableModel.getValueAt(row, 0);
        Client selected = getClientById(id);
        if (selected == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete " + selected.getName() + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            clientManager.removeClient(selected);
            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "✅ Client deleted!");
        }
    }

    private void searchClients() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) { refreshTable(); return; }
        tableModel.setRowCount(0);
        for (Client c : clientManager.searchClients(keyword)) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress()});
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        nameField.setText((String) tableModel.getValueAt(row, 1));
        emailField.setText((String) tableModel.getValueAt(row, 2));
        phoneField.setText((String) tableModel.getValueAt(row, 3));
        addressField.setText((String) tableModel.getValueAt(row, 4));
    }

    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        table.clearSelection();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Client c : clientManager.getAllClients()) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress()});
        }
    }

    private Client getClientById(String id) {
        for (Client c : clientManager.getAllClients()) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }
}
