package com.greenloop.model;

public class Order {

    private int orderId;
    private String clientName;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double total;

    public Order(int orderId,
                 String clientName,
                 String productName,
                 int quantity,
                 double unitPrice) {

        this.orderId = orderId;
        this.clientName = clientName;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;

        this.total = quantity * unitPrice;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId +
                " | Client: " + clientName +
                " | Product: " + productName +
                " | Qty: " + quantity +
                " | Total: Rs." + total;
    }
}
