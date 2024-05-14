package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

@RestController
public class Task1CSVToTableController {

    @Autowired
    DataSource ds;

    @GetMapping("/customers")
    public String csvToTable() {
        String customersCsvFilePath = "/Users/purnachandrarao/Downloads/task/customers.csv";

        String line = "";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(customersCsvFilePath));
            String headers = br.readLine();
            String[] headersArr = headers.split(",");
            String createCustTable = "CREATE TABLE customers ("+headersArr[0]+" int, "+headersArr[1]+" varchar, "+headersArr[2]+" varchar, "+headersArr[3]+" varchar, "+headersArr[4]+" varchar, "+headersArr[5]+" varchar, "+headersArr[6]+" varchar, "+headersArr[7]+" varchar);";
            initDatabaseUsingSpring(createCustTable);
            while ((line = br.readLine()) != null)
            {
                String[] customers = line.split(",");
                StringBuffer insertQuery = new StringBuffer("INSERT INTO customers VALUES ");
                        insertQuery.append("(").append(customers[0]).append(",")
                        .append("'").append(customers[1]).append("',")
                        .append("'").append(customers[2]).append("',")
                        .append("'").append(customers[3]).append("',")
                        .append("'").append(customers[4]).append("',")
                        .append("'").append(customers[5]).append("',")
                        .append("'").append(customers[6]).append("',")
                        .append("'").append(customers[7]).append("');");
                System.out.println("Insert Query ::: " + insertQuery.toString());
                initDatabaseUsingSpring(insertQuery.toString());
            }
            return "Customers data inserted successfully !!!";
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Customers data insertion failed !!!";
    }

    @GetMapping("/orders")
    public String orderCsvToTable() {
        String customersCsvFilePath = "/Users/purnachandrarao/Downloads/task/orders.csv";

        String line = "";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(customersCsvFilePath));
            String headers = br.readLine();
            String[] headersArr = headers.split(",");
            String createOrdersTable = "CREATE TABLE orders ("+headersArr[0]+" int, "+headersArr[1]+" varchar, "+headersArr[2]+" int, "+headersArr[3]+" double);";
            initDatabaseUsingSpring(createOrdersTable);
            while ((line = br.readLine()) != null)
            {
                String[] customers = line.split(",");
                StringBuffer insertQuery = new StringBuffer("INSERT INTO orders VALUES ");
                insertQuery.append("(").append(customers[0]).append(",")
                        .append("'").append(customers[1]).append("',")
                        .append(customers[2]).append(",")
                        .append(customers[3]).append(");");
                System.out.println("Insert Query ::: " + insertQuery.toString());
                initDatabaseUsingSpring(insertQuery.toString());
            }
            return "Orders data inserted successfully !!!";
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Orders data insertion failed !!!";
    }

    private void initDatabaseUsingSpring(String queryString) {
        try (Connection conn = ds.getConnection()) {
            conn.createStatement().execute(queryString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
