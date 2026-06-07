package com.greenloop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OrderDAO {

    public void saveOrder(
            int orderId,
            String client,
            String product,
            int qty,
            double price,
            double total) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "INSERT INTO orders VALUES(?,?,?,?,?,?)";

            PreparedStatement pst =
                    con.prepareStatement(sql);

            pst.setInt(1, orderId);
            pst.setString(2, client);
            pst.setString(3, product);
            pst.setInt(4, qty);
            pst.setDouble(5, price);
            pst.setDouble(6, total);

            pst.executeUpdate();

            System.out.println(
                    "Order Saved!"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
