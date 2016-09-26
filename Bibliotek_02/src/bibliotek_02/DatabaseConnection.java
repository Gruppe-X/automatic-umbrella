package bibliotek_02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  Class for handling the connection with a SQL database.
 *  @author Robert
 */
public class DatabaseConnection {
    private final String connString;
    private Connection connection;
    
    /**
     * Create a DatabaseHandler object, takes the SQL connection string as a
     * String parameter.
     * @param connString The SQL connection string.
     */
    public DatabaseConnection(String connString){
        this.connString = connString;
    }
    
    /**
     * Connects the database handler to the sql server.
     * @return boolean true if connection were successfull, otherwise false.
     * @throws SQLException if a database access error occur or the url is null.
     */
    public boolean connect() throws SQLException {
        boolean success = false;
        try{
            connection = DriverManager.getConnection(connString);
            success = true;
        } catch (SQLException SQLEx) {
            success = false;
            throw SQLEx;
        }
        return success;
    }
    
    public ResultSet query(String statement) throws SQLException {
        ResultSet results;
        PreparedStatement prepStatement = connection.prepareStatement(statement);
        results = prepStatement.executeQuery();
        return results;
    }
    
    public PreparedStatement getStatement(String statement) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement(statement);
        return prepStatement;
    }
    
    public boolean isConnectionValid(){
        boolean result;
        try{
            result = connection.isValid(0);
        } catch (SQLException SQLEx) {
            result = false;
        }
        return result;
    }
    
}
