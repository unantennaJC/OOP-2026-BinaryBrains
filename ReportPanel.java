package com.greenloop.ui;

import com.greenloop.dao.ReportDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportPanel extends JPanel {

    private ReportDAO reportDAO = new ReportDAO();

    public ReportPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Summary cards panel ----
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Overall Summary"));

        JLabel lblRevenue = new JLabel("Total Revenue: Rs." + String.format("%.2f", reportDAO.getTotalRevenue()), SwingConstants.CENTER);
        JLabel lblOrders = new JLabel("Total Orders: " + reportDAO.getTotalOrders(), SwingConstants.CENTER);
        JLabel lblClients = new JLabel("Total Clients: " + reportDAO.getTotalClients(), SwingConstants.CENTER);

        lblRevenue.setFont(new Font("Arial", Font.BOLD, 14));
        lblOrders.setFont(new Font("Arial", Font.BOLD, 14));
        lblClients.setFont(new Font("Arial", Font.BOLD, 14));

        lblRevenue.setBorder(BorderFactory.createEtchedBorder());
        lblOrders.setBorder(BorderFactory.createEtchedBorder());
        lblClients.setBorder(BorderFactory.createEtchedBorder());

        summaryPanel.add(lblRevenue);
        summaryPanel.add(lblOrders);
        summaryPanel.add(lblClients);

        // ---- Tabbed reports ----
        JTabbedPane reportTabs = new JTabbedPane();

        // Monthly revenue tab
        String[] monthlyColumns = {"Month", "Total Orders", "Total Revenue (Rs.)"};
        DefaultTableModel monthlyModel = new DefaultTableModel(monthlyColumns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable monthlyTable = new JTable(monthlyModel);
        loadMonthlyData(monthlyModel);
        reportTabs.addTab("Monthly Revenue", new JScrollPane(monthlyTable));

        // Low stock tab
        String[] lowStockColumns = {"Product Name", "Quantity on Hand", "Reorder Level"};
        DefaultTableModel lowStockModel = new DefaultTableModel(lowStockColumns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable lowStockTable = new JTable(lowStockModel);
        loadLowStockData(lowStockModel);
        reportTabs.addTab("Low Stock Alerts", new JScrollPane(lowStockTable));

        // Top selling tab
        String[] topSellingColumns = {"Product Name", "Total Sold", "Total Revenue (Rs.)"};
        DefaultTableModel topSellingModel = new DefaultTableModel(topSellingColumns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable topSellingTable = new JTable(topSellingModel);
        loadTopSellingData(topSellingModel);
        reportTabs.addTab("Top Selling Products", new JScrollPane(topSellingTable));

        // ---- Refresh button ----
        JButton btnRefresh = new JButton("Refresh Reports");
        btnRefresh.addActionListener(e -> {
            lblRevenue.setText("Total Revenue: Rs." + String.format("%.2f", reportDAO.getTotalRevenue()));
            lblOrders.setText("Total Orders: " + reportDAO.getTotalOrders());
            lblClients.setText("Total Clients: " + reportDAO.getTotalClients());
            loadMonthlyData(monthlyModel);
            loadLowStockData(lowStockModel);
            loadTopSellingData(topSellingModel);
            JOptionPane.showMessageDialog(this, "Reports refreshed!");
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnRefresh);

        add(summaryPanel, BorderLayout.NORTH);
        add(reportTabs, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadMonthlyData(DefaultTableModel model) {
        model.setRowCount(0);
        List<String[]> data = reportDAO.getMonthlySummary();
        for (String[] row : data) {
            model.addRow(row);
        }
    }

    private void loadLowStockData(DefaultTableModel model) {
        model.setRowCount(0);
        List<String[]> data = reportDAO.getLowStockReport();
        for (String[] row : data) {
            model.addRow(row);
        }
    }

    private void loadTopSellingData(DefaultTableModel model) {
        model.setRowCount(0);
        List<String[]> data = reportDAO.getTopSellingProducts();
        for (String[] row : data) {
            model.addRow(row);
        }
    }
}
