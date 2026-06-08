package com.greenloop.model;

public class Order {
    private int orderId;
    private int clientId;
    private String clientName;
    private String orderDate;
    private String status;
    private double totalAmount;

    public Order(int orderId, int clientId, String clientName,
                 String orderDate, String status, double totalAmount) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.clientName = clientName;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public int getOrderId() { return orderId; }
    public int getClientId() { return clientId; }
    public String getClientName() { return clientName; }
    public String getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }

    public void setStatus(String status) { this.status = status; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}
