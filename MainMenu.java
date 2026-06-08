package com.greenloop.ui;

import javax.swing.*;

public class MainMenu {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("GreenLoop - Eco Packaging Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Delivery Agents", new DeliveryAgentPanel());
            // Your group members will add their tabs here:
            // tabs.addTab("Products", new ProductPanel());
            // tabs.addTab("Clients", new ClientPanel());
            // tabs.addTab("Orders", new OrderPanel());

            frame.add(tabs);
            frame.setVisible(true);
        });
    }
}