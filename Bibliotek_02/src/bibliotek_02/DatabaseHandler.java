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
    /**
    public ResultSet executeQuery(String statement) {
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
    
    public ResultSet executeQuery(PreparedStatement statement){
        ResultSet results = null;
        try {
            results = statement.executeQuery();
        } catch (SQLException SQLEx) {
            System.out.println(SQLEx.getMessage());
            SQLEx.printStackTrace();
        }
        return results;
    }
    **/
    
    /**
     * Search a given table and column for a given String value.
     * @param table Table to search
     * @param col Column to search
     * @param parameter Value to search for.
     * @return Returns a ResultSet with the results.
     */
    public ResultSet searchTableByColumnValString(String table, String col, String parameter){
        ResultSet results = null;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ? WHERE ? = ?");
            statement.setString(1, table);
            statement.setString(2, col);
            statement.setString(3, parameter);
            results = statement.executeQuery();
        } catch (SQLException SQLEx) {
            System.out.println(SQLEx.getMessage());
            SQLEx.printStackTrace();
        }
        return results;
    }
    
    /**
     * Search a given table and column for a given Int value.
     * @param table Table to search
     * @param col Column to search
     * @param parameter Value to search for.
     * @return Returns a ResultSet with the results.
     */
    public ResultSet searchTableByColumnValInt(String table, String col, int parameter){
        ResultSet results = null;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ? WHERE ? = ?");
            statement.setString(1, table);
            statement.setString(2, col);
            statement.setInt(3, parameter);
            results = statement.executeQuery();
        } catch (SQLException SQLEx) {
            System.out.println(SQLEx.getMessage());
            SQLEx.printStackTrace();
        }
        return results;
    }
    
    /**
     * Gets the ResultSet object returned after executing a PreparedStatement object. 
     * @param preparedStatement 
     * @return 
     */
    public ResultSet getResultSet(String preparedStatement){
        ResultSet results = null;
        try{
            PreparedStatement statement = connection.prepareStatement(preparedStatement);
            results = statement.executeQuery();
        } catch (SQLException SQLEx) {
            System.out.println(SQLEx.getMessage());
            SQLEx.printStackTrace();
        }
        return results;
    }
    
    public ResultSet getEmployees(){
        return getResultSet("SELECT * FROM Ansatt");
    }
    
    public ResultSet getEmployeesByID(int id){
        return searchTableByColumnValInt("Ansatt", "AnsattID", id);
    }
    
    public ResultSet getEmployeesByFirstName(String firstName){
        return searchTableByColumnValString("Ansatt", "Fornavn", firstName);
    }
    
    public ResultSet getEmployeesByLastName(String lastName){
        return searchTableByColumnValString("Ansatt", "Etternavn", lastName);
    }
    
    public ResultSet getBorrowers(){
        return getResultSet("SELECT * FROM Lånetaker");
    }
    
    public ResultSet getBorrowersByID(int id){
        return searchTableByColumnValInt("Lånetaker", "LånetakerID", id);
    }
    
    public ResultSet getBorrowersByFirstName(String firstName){
        return searchTableByColumnValString("Lånetaker", "Fornavn", firstName);
    }
    
    public ResultSet getBorrowersByLastName(String lastName){
        return searchTableByColumnValString("Lånetaker", "Etternavn", lastName);
    }
    
    public ResultSet getBooks(){
        return getResultSet("SELECT * FROM Bok");
    }
    
    public ResultSet getBooksByID(String ISBN){
        return searchTableByColumnValString("Bok", "ISBN", ISBN);
    }
    
    public ResultSet getBooksByTitle(String title){
        return searchTableByColumnValString("Bok", "Tittel", title);
    }
    
    public ResultSet getBooksByPublisher(String publisher){
        return searchTableByColumnValString("Bok", "Forlag", publisher);
    }
    
    public ResultSet getBooksByAuthor(String publisher){
        return searchTableByColumnValString("Bok", "Forlag", publisher);
    }
    
    public ResultSet getCopys(){
        return getResultSet("SELECT * FROM Eksemplar");
    }
    
    public ResultSet getCopysByID(int id){
        return searchTableByColumnValInt("Eksemplar", "EksemplarID", id);
    }
    
    public ResultSet getCopysByIDBN(String ISBN){
        return searchTableByColumnValString("Eksemplar", "ISBN", ISBN);
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
