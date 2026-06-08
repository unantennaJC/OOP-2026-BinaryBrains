package com.greenloop.model;

public class Delivery {
    private int deliveryId;
    private int orderId;
    private int agentId;
    private String agentName;
    private String clientName;
    private String assignedDate;
    private String deliveryStatus;

    public Delivery(int deliveryId, int orderId, int agentId, String agentName,
                    String clientName, String assignedDate, String deliveryStatus) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.agentId = agentId;
        this.agentName = agentName;
        this.clientName = clientName;
        this.assignedDate = assignedDate;
        this.deliveryStatus = deliveryStatus;
    }

    public int getDeliveryId() { return deliveryId; }
    public int getOrderId() { return orderId; }
    public int getAgentId() { return agentId; }
    public String getAgentName() { return agentName; }
    public String getClientName() { return clientName; }
    public String getAssignedDate() { return assignedDate; }
    public String getDeliveryStatus() { return deliveryStatus; }

    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }
}
