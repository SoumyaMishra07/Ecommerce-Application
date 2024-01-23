package com.example.ecommerce;

import java.sql.ResultSet;
import java.util.concurrent.CyclicBarrier;

public class Login {
    public Customer customerLogin(String userName, String password){

        String query = "SELECT * FROM customer where email = '"+userName+"' AND password = '"+password+"'";
        DbConnection dbConnection = new DbConnection();
        try {
            ResultSet rs = dbConnection.getQueryTable(query);
            if (rs.next()){
                return new Customer(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("mobile"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Login login = new Login();
        Customer customer = login.customerLogin("soumya@gmail.com", "soumya@1999");
        System.out.println("Welcome : " + customer.getName());
        //System.out.println(login.customerLogin("soumya@gmail.com", "soumya@1999"));
    }
}