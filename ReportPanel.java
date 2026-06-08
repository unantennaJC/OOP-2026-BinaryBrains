// ReportPanel.java - Task 7: Generate Monthly Sales & Inventory Reports
// Shows low-stock alerts and revenue summaries

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

public class ReportPanel extends JPanel {

    private ClientManager clientManager;
    private JTextArea summaryArea;
    private JTable lowStockTable;
    private DefaultTableModel lowStockModel;

    // Sample product data for demonstration (in real project this comes from ProductManager)
    private static final String[][] SAMPLE_PRODUCTS = {
        {"P001", "Biodegradable Wrap",  "150", "50",  "2500.00"},
        {"P002", "Recycled Boxes",      "30",  "100", "4500.00"},
        {"P003", "Compostable Bags",    "200", "80",  "3200.00"},
        {"P004", "Eco Bubble Wrap",     "20",  "60",  "1800.00"},
        {"P005", "Paper Tape Rolls",    "500", "100", "900.00"},
        {"P006", "Green Mailers",       "15",  "50",  "1200.00"},
    };

    public ReportPanel(ClientManager clientManager) {
        this.clientManager = clientManager;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 250, 245));

        add(buildTopPanel(),     BorderLayout.NORTH);
        add(buildMainPanel(),    BorderLayout.CENTER);

        generateReport();
    }

    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(34, 139, 34));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JLabel title = new JLabel("📊 Monthly Sales & Inventory Reports - GreenLoop");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JButton refreshBtn = new JButton("🔄 Refresh Report");
        refreshBtn.setBackground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.addActionListener(e -> generateReport());

        JButton exportBtn = new JButton("💾 Export Report");
        exportBtn.setBackground(Color.WHITE);
        exportBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exportBtn.addActionListener(e -> exportReport());

        panel.add(title);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(refreshBtn);
        panel.add(exportBtn);
        return panel;
    }

    private JPanel buildMainPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(245, 250, 245));

        // Left: Summary
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("📋 Monthly Summary Report"));
        summaryPanel.setBackground(Color.WHITE);

        summaryArea = new JTextArea();
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        summaryArea.setEditable(false);
        summaryArea.setBackground(new Color(250, 255, 250));
        summaryArea.setMargin(new Insets(10, 10, 10, 10));
        summaryPanel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        // Right: Low Stock Alerts
        JPanel stockPanel = new JPanel(new BorderLayout());
        stockPanel.setBorder(BorderFactory.createTitledBorder("⚠️ Low Stock Alerts"));
        stockPanel.setBackground(Color.WHITE);

        String[] cols = {"Product ID", "Product Name", "In Stock", "Reorder Level", "Status"};
        lowStockModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        lowStockTable = new JTable(lowStockModel) {
            // Color rows red if critically low
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                String status = (String) getModel().getValueAt(row, 4);
                if ("CRITICAL".equals(status)) {
                    c.setBackground(new Color(255, 200, 200));
                } else if ("LOW".equals(status)) {
                    c.setBackground(new Color(255, 235, 180));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        lowStockTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lowStockTable.setRowHeight(25);
        lowStockTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        lowStockTable.getTableHeader().setBackground(new Color(220, 50, 50));
        lowStockTable.getTableHeader().setForeground(Color.WHITE);
        stockPanel.add(new JScrollPane(lowStockTable), BorderLayout.CENTER);

        // Legend
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT));
        legend.setBackground(Color.WHITE);
        legend.add(makeLabel("🔴 CRITICAL = Below reorder level", new Color(255, 200, 200)));
        legend.add(makeLabel("🟡 LOW = Near reorder level",       new Color(255, 235, 180)));
        stockPanel.add(legend, BorderLayout.SOUTH);

        panel.add(summaryPanel);
        panel.add(stockPanel);
        return panel;
    }

    private JLabel makeLabel(String text, Color bg) {
        JLabel lbl = new JLabel("  " + text + "  ");
        lbl.setOpaque(true);
        lbl.setBackground(bg);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return lbl;
    }

    // Generate the report content
    private void generateReport() {
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"));

        // Calculate totals from sample data
        double totalRevenue = 0;
        int    lowStockCount = 0;
        int    criticalCount = 0;

        lowStockModel.setRowCount(0);

        for (String[] p : SAMPLE_PRODUCTS) {
            int    qty      = Integer.parseInt(p[2]);
            int    reorder  = Integer.parseInt(p[3]);
            double price    = Double.parseDouble(p[4]);
            totalRevenue   += price;

            String status;
            if (qty < reorder / 2) {
                status = "CRITICAL";
                criticalCount++;
                lowStockCount++;
                lowStockModel.addRow(new Object[]{p[0], p[1], qty, reorder, status});
            } else if (qty < reorder) {
                status = "LOW";
                lowStockCount++;
                lowStockModel.addRow(new Object[]{p[0], p[1], qty, reorder, status});
            }
        }

        int totalClients  = clientManager.getTotalClients();
        int totalProducts = SAMPLE_PRODUCTS.length;

        // Build summary text
        StringBuilder sb = new StringBuilder();
        sb.append("============================================\n");
        sb.append("   GREENLOOP - MONTHLY REPORT\n");
        sb.append("   ").append(month).append("\n");
        sb.append("   Generated: ").append(LocalDate.now()).append("\n");
        sb.append("============================================\n\n");

        sb.append("📦 INVENTORY SUMMARY\n");
        sb.append("--------------------------------------------\n");
        sb.append(String.format("  Total Products       : %d\n", totalProducts));
        sb.append(String.format("  Low Stock Items      : %d\n", lowStockCount));
        sb.append(String.format("  Critical Stock Items : %d\n\n", criticalCount));

        sb.append("👥 CLIENT SUMMARY\n");
        sb.append("--------------------------------------------\n");
        sb.append(String.format("  Total Active Clients : %d\n\n", totalClients));

        sb.append("💰 REVENUE SUMMARY\n");
        sb.append("--------------------------------------------\n");
        sb.append(String.format("  Total Revenue        : LKR %.2f\n\n", totalRevenue));

        sb.append("⚠️  ALERTS\n");
        sb.append("--------------------------------------------\n");
        if (criticalCount > 0) {
            sb.append("  ❗ ").append(criticalCount).append(" product(s) are CRITICALLY low!\n");
            sb.append("     Please reorder immediately.\n");
        }
        if (lowStockCount == 0) {
            sb.append("  ✅ All stock levels are healthy.\n");
        }
        sb.append("\n============================================\n");
        sb.append("          END OF REPORT\n");
        sb.append("============================================\n");

        summaryArea.setText(sb.toString());
    }

    // Export report to a text file
    private void exportReport() {
        String filename = "GreenLoop_Report_" + LocalDate.now() + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.print(summaryArea.getText());
            JOptionPane.showMessageDialog(this,
                "✅ Report exported successfully!\nFile: " + filename,
                "Export Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error exporting report: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
