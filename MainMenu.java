package com.greenloop.ui;

import javax.swing.*;
import java.awt.*;

public class MainMenu {
    public static void main(String[] args) {
        UITheme.applyTheme();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("GreenLoop - Eco Packaging Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 680);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().setBackground(UITheme.LIGHT_GRAY);

            // Header panel
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(UITheme.PRIMARY_GREEN);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

            JLabel titleLabel = new JLabel("🌿 GreenLoop - Eco Packaging Management");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel, BorderLayout.WEST);

            JLabel subtitleLabel = new JLabel("Sustainable Packaging Supply System");
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            subtitleLabel.setForeground(new Color(200, 230, 201));
            headerPanel.add(subtitleLabel, BorderLayout.EAST);

            // Tabs
            JTabbedPane tabs = new JTabbedPane();
            tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
            tabs.addTab("📦 Products", new ProductPanel());
            tabs.addTab("👥 Clients", new ClientPanel());
            tabs.addTab("🏭 Stock", new StockPanel());
            tabs.addTab("🚚 Delivery Agents", new DeliveryAgentPanel());
            tabs.addTab("🛒 Orders", new OrderPanel());
            tabs.addTab("📋 Assignments", new DeliveryAssignmentPanel());
            tabs.addTab("📊 Reports", new ReportPanel());

            frame.add(headerPanel, BorderLayout.NORTH);
            frame.add(tabs, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}