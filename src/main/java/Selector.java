package hello;

import java.sql.ResultSet;

/**
 * Class that selects from the database
 * @author Zach Panzarino
 * @version 1.0.0
 */
public class Selector extends Worker {
    /**
     * Creates a new Selector with a database connection
     * @param url The database URL
     * @param username The database username
     * @author Jake Sylvestre
     * @param password The database password
     */
    public Selector(String url, String username, String password){
        super(url, username, password);
    }
    /**
     * Select from database
     * @param select What to be selected
     * @param from What table to use
     * @author Jake Sylvestre
     * @return Results of executed query
     */
    public ResultSet select(String select, String from){
        String query = "SELECT `"+select+"` FROM  `"+from+"`";
        return execute(query);
    }
    /**
     * Select from database
     * @author Jake Sylvestre
     * @param select What to be selected
     * @param from What table to use
     * @param where Condition for selection
     * @return Results of executed query
     */
    public ResultSet select(String select, String from, String where){
        String query = "SELECT `"+select+"` FROM  `"+from+"` WHERE "+where;
        return execute(query);
    }
    /**
     * Select from database
     * @param select What to be selected
     * @param from What table to use
     * @param where Condition for selection
     * @param limit Limit for selection
     * @return Results of executed query
     */
    public ResultSet select(String select, String from, String where, String limit){
        String query = "SELECT `"+select+"` FROM  `"+from+"` WHERE "+where+" LIMIT "+limit;
        return execute(query);
    }
}
