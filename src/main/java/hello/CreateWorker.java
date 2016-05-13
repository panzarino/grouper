package hello;

import java.sql.ResultSet;

/**
 * Class that creates new tables in the database
 * @author Zach Panzarino
 * @version 1.0.0
 */
public class CreateWorker extends Worker {
    /**
     * Creates a new Creator with a database connection
     * @param url The database URL
     * @param username The database username
     * @param password The database password
     */
    public CreateWorker(String url, String username, String password){
        super(url, username, password);
    }
    /**
     * Create a new table for a chat
     * @param id The ID of the chat
     * @return Results of executed query
     */
    public ResultSet create(int id){
        String query = "CREATE TABLE IF NOT EXISTS `Chat_"+id+"` ("+
          "`ID` smallint(5) unsigned NOT NULL AUTO_INCREMENT,"+
          "`Name` varchar(18) NOT NULL,"+
          "`Number` varchar(15) NOT NULL,"+
          "PRIMARY KEY (`ID`)"+
        ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";
        return execute(query);
    }
}