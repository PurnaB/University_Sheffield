package com.example.demo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Task2SearchController {

    private static final Logger log = LoggerFactory.getLogger(Task2SearchController.class);

    @Autowired
    DataSource ds;

    @GetMapping (value = "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAll (@RequestParam String status) {
        String query;
        try {
            if (status != null && !status.isEmpty()) {
                if (status.equalsIgnoreCase("all")){
                    query = "SELECT * FROM customers cust, orders ord where cust.customer_id=ord.customer_id order by customer_id";
                } else {
                    query = "SELECT * FROM customers cust, orders ord where cust.customer_id=ord.customer_id and cust.status = '"+status+"' order by customer_id";
                }
            } else {
                query = "SELECT * FROM customers cust, orders ord where cust.customer_id=ord.customer_id order by customer_id";
            }
            return getData(query);
        } catch (Exception e) {

        }
        return "No Data !!!";
    }

    @GetMapping (value = "/getbyid", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getbyid (@RequestParam String id, @RequestParam String status) {
        String query;
        try {
            if(id == null || id.isEmpty()) {
                return "Customer ID should not be empty !!!";
            }
            if (status != null && !status.isEmpty()) {
                if (status.equalsIgnoreCase("all")){
                    query = "SELECT * FROM customers cust, orders ord where cust.customer_id=" +id+" order by customer_id";
                } else {
                    query = "SELECT * FROM customers cust, orders ord where cust.customer_id=" +id+" and cust.status = '"+status+"' order by customer_id";
                }
            } else {
                query = "SELECT * FROM customers cust, orders ord where cust.customer_id=" +id+" order by customer_id";
            }
            return getData(query);
        } catch (Exception e) {

        }
        return "No Data !!!";
    }

    private String getData(String queryString) {
        try (Connection conn = ds.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(queryString);
            ResultSet rs = preparedStatement.executeQuery();
            List<JsonObject> jsonArray = new ArrayList<>();
            while (rs.next()) {
                JsonObject obj = new JsonObject();
                //obj.addProperty("customer_id", rs.getInt("customer_id"));
                obj.addProperty("firstname", rs.getString("firstname"));
                obj.addProperty("surname", rs.getString("surname"));
                obj.addProperty("email", rs.getString("email"));
                obj.addProperty("address", rs.getString("address"));
                obj.addProperty("zip_code", rs.getString("zip_code"));
                obj.addProperty("region", rs.getString("region"));
                obj.addProperty("status", rs.getString("status"));
                obj.addProperty("order_id", rs.getInt("order_id"));
                obj.addProperty("date", rs.getString("date"));
                obj.addProperty("amount", rs.getDouble("amount"));
                jsonArray.add(obj);
            }
            Gson gson = new Gson();
            return gson.toJson(jsonArray);
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        return "No data !!!";
    }
}
