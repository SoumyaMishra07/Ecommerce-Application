package com.example.ecommerce;

import javafx.collections.ObservableList;

import java.sql.ResultSet;

public class Order {
    public static boolean placeOrder(Customer customer, Product product){
        String groupOrderId = "SELECT max(group_order_id) +1 id FROM orders";
        DbConnection dbConnection = new DbConnection();
        try {
            ResultSet rs = dbConnection.getQueryTable(groupOrderId);
            if (rs.next()){
                String placeOrder = "INSERT INTO orders(group_order_id, customer_id, product_id) VALUES("+rs.getInt("id")+","+customer.getId()+","+product.getId()+")";
                return dbConnection.updateDatabase(placeOrder) != 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static int placeMultipleOrder(Customer customer, ObservableList<Product> ProductList){
        String groupOrderId = "SELECT max(group_order_id) +1 id FROM orders";
        DbConnection dbConnection = new DbConnection();
        try {
            ResultSet rs = dbConnection.getQueryTable(groupOrderId);
            int count = 0;

            if (rs.next()){
                for (Product product : ProductList){
                    String placeOrder = "INSERT INTO orders(group_order_id, customer_id, product_id) VALUES("+rs.getInt("id")+","+customer.getId()+","+product.getId()+")";
                    count += dbConnection.updateDatabase(placeOrder);
                }
                return count;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

}