package com.greenloop.model;

public class DeliveryAgent{

    private String agentName;
    private String vehicleNo;

    public DeliveryAgent(String agentName, String vehicleNo){
        this.agentName = agentName;
        this.vehicleNo = vehicleNo;
    }

    @Override
    public String toString(){
        return agentName + " (" + vehicleNo + ")";
    }
}
