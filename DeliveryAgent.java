package com.greenloop.model;

public class DeliveryAgent {
    private int agentId;
    private String name;
    private String email;
    private String phone;
    private String vehicleType;
    private String vehiclePlate;

    public DeliveryAgent(int agentId, String name, String email,
                         String phone, String vehicleType, String vehiclePlate) {
        this.agentId = agentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.vehicleType = vehicleType;
        this.vehiclePlate = vehiclePlate;
    }

    public int getAgentId() { return agentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getVehicleType() { return vehicleType; }
    public String getVehiclePlate() { return vehiclePlate; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }
}