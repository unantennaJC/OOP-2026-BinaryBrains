// GreenLoopApp.java - Main Application
// This is where the program starts
// Run this file to launch the GreenLoop application

import javax.swing.*;
import java.awt.*;

public class GreenLoopApp extends JFrame {

    private ClientManager clientManager;

    public GreenLoopApp() {
        clientManager = new ClientManager();

        setTitle("🌿 GreenLoop - Eco-Friendly Packaging Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(new Color(236, 240, 241));

        // Task 2 - Manage Clients
        tabs.addTab("👥 Manage Clients", new ClientPanel(clientManager));

        // Task 7 - Reports
        tabs.addTab("📊 Reports", new ReportPanel(clientManager));

        // Placeholder tabs for other group members' tasks
        tabs.addTab("📦 Products",        makePlaceholder("Task 1 - Product Catalogue (Other member)"));
        tabs.addTab("🏭 Inventory",       makePlaceholder("Task 3 - Stock/Inventory (Other member)"));
        tabs.addTab("🚚 Delivery Agents", makePlaceholder("Task 4 - Delivery Agents (Other member)"));
        tabs.addTab("🛒 Orders",          makePlaceholder("Task 5 - Client Orders (Other member)"));
        tabs.addTab("📬 Assign Delivery", makePlaceholder("Task 6 - Assign Delivery (Other member)"));
        tabs.addTab("📧 Notifications",   makePlaceholder("Tasks 8 & 9 - Email Notifications (Other member)"));

        add(tabs);
        setVisible(true);
    }

    private JPanel makePlaceholder(String text) {
        JPanel p = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(Color.GRAY);
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new GreenLoopApp());
    }
}
