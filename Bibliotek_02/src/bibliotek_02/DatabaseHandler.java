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
    private DatabaseConnection conn;
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
        ResultSet results;
        PreparedStatement prepStatement = connection.prepareStatement(statement);
        results = prepStatement.executeQuery();
        return results;
    }
    
    public PreparedStatement getStatement(String statement) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement(statement);
        return prepStatement;
    }
    
    public ResultSet getAnsatte(){
        ResultSet results = null;
        try{
            PreparedStatement statement = conn.getStatement("SELECT * FROM Ansatt");
            results = statement.executeQuery();
            int i = 0;
            while(results.next()){
                System.out.println(results.getString(2));
                i++;
            }
        } catch (SQLException SQLEx) {
            System.out.println(SQLEx.getMessage());
        }
        return results;
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
