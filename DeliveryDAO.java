package com.greenloop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeliveryDAO {

    public void assignAgent(
            int orderId,
            int agentId) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "INSERT INTO delivery_assignments(order_id,agent_id) VALUES (?,?)";

            PreparedStatement pst =
                    con.prepareStatement(sql);

            pst.setInt(1, orderId);
            pst.setInt(2, agentId);

            pst.executeUpdate();

            System.out.println(
                    "Agent Assigned!"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
