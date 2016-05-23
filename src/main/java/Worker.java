package hello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Class that interacts with the database
 * @author Zach Panzarino
 * @version 1.0.0
 */
public class Worker {
    private static final Logger log = Logger.getLogger(Worker.class.getName());
    Connection conn;
    /**
     * Creates a new Worker with a database connection
     * @param url The database URL
     * @param username The database username
     * @param password The database password
     */
    public Worker(String url, String username, String password){
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
        }
    }
    /**
     * Execute an SQL statement with database
     * @param statement The SQL statement to be executed
     * @return Results of executed query
     */
    public ResultSet execute(String statement){
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(statement);
            stmt.close();
            return rs;
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
            return null;
        }
    }
    /**
     * Close the database connection
     */
    public void close(){
        try {
            conn.close();
        } catch (SQLException ex) {
            BasicConfigurator.configure();
            log.info("SQLException: " + ex.getMessage());
            log.info("SQLState: " + ex.getSQLState());
            log.info("VendorError: " + ex.getErrorCode());
        }
    }
}