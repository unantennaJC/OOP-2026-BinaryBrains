package ui;

import database.OrderDAO;
import database.DeliveryDAO;

import javax.swing.*;
import java.awt.*;

public class OrderDeliveryUI extends JFrame {

    JTextField txtClient;
    JTextField txtProduct;
    JTextField txtQty;
    JTextField txtPrice;

    JLabel lblTotal;

    JComboBox<String> cmbAgents;

    public OrderDeliveryUI() {

        setTitle("GreenLoop Order Management");

        setSize(600,400);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new GridLayout(7,2));

        add(new JLabel("Client Name"));
        txtClient = new JTextField();
        add(txtClient);

        add(new JLabel("Product Name"));
        txtProduct = new JTextField();
        add(txtProduct);

        add(new JLabel("Quantity"));
        txtQty = new JTextField();
        add(txtQty);

        add(new JLabel("Unit Price"));
        txtPrice = new JTextField();
        add(txtPrice);

        add(new JLabel("Delivery Agent"));

        cmbAgents = new JComboBox<>();

        cmbAgents.addItem("1 - Zaid");
        cmbAgents.addItem("2 - Aazim");
        cmbAgents.addItem("3 - Rifdhi");
        cmbAgents.addItem("4 - Akthar");


        add(cmbAgents);

        JButton btnSave =
                new JButton("Process Order");

        JButton btnAssign =
                new JButton("Assign Agent");

        add(btnSave);
        add(btnAssign);

        lblTotal = new JLabel("Total = Rs.0");
        add(lblTotal);

        btnSave.addActionListener(e -> processOrder());

        btnAssign.addActionListener(e -> assignAgent());
    }

    private void processOrder() {

        String client =
                txtClient.getText();

        String product =
                txtProduct.getText();

        int qty =
                Integer.parseInt(txtQty.getText());

        double price =
                Double.parseDouble(txtPrice.getText());

        double total =
                qty * price;

        lblTotal.setText(
                "Total = Rs." + total);

        OrderDAO dao =
                new OrderDAO();

        dao.saveOrder(
                client,
                product,
                qty,
                price,
                total);

        JOptionPane.showMessageDialog(
                this,
                "Order Saved Successfully");
    }

    private void assignAgent() {

        String selected =
                cmbAgents.getSelectedItem().toString();

        int agentId =
                Integer.parseInt(
                        selected.substring(0,1));

        String orderIdInput =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Order ID");

        int orderId =
                Integer.parseInt(orderIdInput);

        DeliveryDAO dao =
                new DeliveryDAO();

        dao.assignDelivery(
                orderId,
                agentId);

        JOptionPane.showMessageDialog(
                this,
                "Delivery Agent Assigned");
    }
}
