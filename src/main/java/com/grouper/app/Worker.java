package com.grouper.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Class that interacts with the database
 * @author Zach Panzarino
 * @version 1.0.0
 */
public class Worker {
    Connection conn;
    /**
     * Created a new Worker with a database connection
     * @param url The database URL
     * @param username The database username
     * @param password The database password
     */
    public Worker(String url, String username, String password){
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    /**
     * Execute an SQL statement with database
     * @param statement The SQL statement to be executed
     * @return Results of executed query
     */
    public ResultSet execute(String statement){
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        stmt.close();
        return rs;
    }
}