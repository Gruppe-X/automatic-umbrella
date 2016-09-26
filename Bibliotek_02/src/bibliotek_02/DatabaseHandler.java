package bibliotek_02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Robert
 */
public class DatabaseHandler {
    
    private final String connString = "jdbc:sqlserver://roberris-prosjektx.uials.no;databaseName=Bibliotek;username=sa;password=password123";
    private Connection connection;
    
    public DatabaseHandler() {
        connect();
    }
    
    /**
     * Connects the database handler to the sql server.
     * @return boolean true if connection were successfull, otherwise false.
     * @throws SQLException if a database access error occur or the url is null.
     */
    private boolean connect() {
        boolean success;
        try{
            connection = DriverManager.getConnection(connString);
            success = true;
        } catch (SQLException SQLEx) {
            success = false;
            System.out.println(SQLEx.getMessage());
            SQLEx.printStackTrace();
        }
        return success;
    }
    
    public ResultSet query(String statement) throws SQLException {
        ResultSet results = null;
        try {
            PreparedStatement prepStatement = connection.prepareStatement(statement);
            results = prepStatement.executeQuery();
        } catch (SQLException SQLEx) {
            System.out.println(SQLEx.getMessage());
            SQLEx.printStackTrace();
        }
        return results;
    }
    
    public PreparedStatement getStatement(String statement) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement(statement);
        return prepStatement;
    }
    
    public ResultSet getResultSet(String preparedStatement){
        ResultSet results = null;
        try{
            PreparedStatement statement = connection.prepareStatement(preparedStatement);
            results = statement.executeQuery();
        } catch (SQLException SQLEx) {
            System.out.println(SQLEx.getMessage());
        }
        return results;
    }
    
    public ResultSet getEmployees(){
        return getResultSet("SELECT * FROM Ansatt");
    }
    
    public ResultSet getBorrowers(){
        return getResultSet("SELECT * FROM Lånetaker");
    }
    
    public ResultSet getBooks(){
        return getResultSet("SELECT * FROM Bok");
    }
    
    public ResultSet getCopys(){
        return getResultSet("SELECT * FROM Eksemplar");
    }
    
    public boolean isConnectionValid(){
        boolean result = false;
        try{
            result = connection.isValid(0);
        } catch (SQLException SQLEx) {
            System.out.println(SQLEx.getMessage());
            SQLEx.printStackTrace();
        }
        return result;
    }
    
}
