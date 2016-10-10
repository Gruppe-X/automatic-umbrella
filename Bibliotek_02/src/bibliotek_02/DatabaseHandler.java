package bibliotek_02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert
 */
public class DatabaseHandler {
    
    private final String connString = "jdbc:sqlserver://roberris-prosjektx.uials.no;databaseName=Bibliotek;username=sa;password=password123";
    private Connection connection;
    private PreparedStatement searchStatement;
    private PreparedStatement bookCopyJoinStatement;
    private PreparedStatement bookQuantityByIDStatement;
    
    public DatabaseHandler() {
        connect();
        try{
            searchStatement = connection.prepareStatement("SELECT * FROM ? WHERE ? = ?");
            bookCopyJoinStatement = connection.prepareStatement("SELECT B.ISBN, Tittel, Forlag, Forfatter, Utgave, Utgivelsesår, E.EksemplarID FROM Bok B RIGHT JOIN Eksemplar E ON B.ISBN = E.ISBN");
            bookQuantityByIDStatement = connection.prepareStatement("SELECT count(ISBN) FROM Eksemplar WHERE ISBN = ?");
        } catch (SQLException SQLEx) {
            System.out.println(SQLEx.getMessage());
            SQLEx.printStackTrace();
        }
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
     * Search a given table and column for a given String value.
     * @param table Table to search
     * @param col Column to search
     * @param parameter Value to search for.
     * @return Returns a ResultSet with the results.
     */
    public ResultSet searchTableByColumnValString(String table, String col, String parameter){
        ResultSet results = null;
        try{
            searchStatement.setString(1, table);
            searchStatement.setString(2, col);
            searchStatement.setString(3, parameter);
            results = searchStatement.executeQuery();
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
            searchStatement.setString(1, table);
            searchStatement.setString(2, col);
            searchStatement.setInt(3, parameter);
            results = searchStatement.executeQuery();
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
    
    /**
     * Returns quantity of books with given id/isbn
     * @param bookID id/isbn to search for quantity of.
     * @return quantity of books with given id/isbn.
     */
    public int getQuantityOfBooksByID(String bookID){
        int quantity = -1;
        try {
            bookQuantityByIDStatement.setString(1, bookID);
            ResultSet result = bookQuantityByIDStatement.executeQuery();
            if(result.next()){
            quantity = result.getInt(1);
            }
        } catch (SQLException SQLEx) {
            System.out.println(SQLEx.getMessage());
            SQLEx.printStackTrace();
        }
        return quantity;
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
        ResultSet results;
        try {
            results = bookCopyJoinStatement.executeQuery();
        } catch (SQLException ex) {
            results = null;
        }
        return results;
    }
    
    /**
     * Returns a List of all borrowers.
     * @return a List of all borrowers.
     */
    public List<Borrower> listBorrowers(){
        List<Borrower> customers = new ArrayList<>();
        ResultSet customerSet = getBorrowers();
        try {
            while(customerSet.next()){
                customers.add(new Borrower(customerSet.getString(2), customerSet.getString(3), customerSet.getString(4)));
            }
        } catch (SQLException SQLEx) {
            //TODO
        }
        return customers;
    }
    
    public List<Librarian> listLibrarians(){
        List<Librarian> librarians = new ArrayList<>();
        ResultSet librarianSet = getEmployees();
        try {
            while(librarianSet.next()){
                librarians.add(new Librarian(librarianSet.getString(1), librarianSet.getString(2), librarianSet.getString(3)));
            }
        } catch (SQLException ex) {
            //TODO
        }
        return librarians;
    }
    
    public List<Book> listBooks(){
        List<Book> books = new ArrayList<>();
        ResultSet bookSet = getBooks();
        try{
            while(bookSet.next()){
                books.add(new Book(bookSet.getString(1), bookSet.getString(2), bookSet.getString(4), bookSet.getString(5), bookSet.getString(6), bookSet.getString(3), getQuantityOfBooksByID(bookSet.getString(1))));
            }
        } catch (SQLException ex) {
            books = null;
        }
        return books;
    }
    
    public List<BookCopy> listBookCopys(){
        List<BookCopy> bookCopys = new ArrayList<>();
        ResultSet bookCopySet = getCopys();
        try {
            while(bookCopySet.next()){
                String bookID = bookCopySet.getString(1);
                String title = bookCopySet.getString(2);
                String publisher = bookCopySet.getString(3);
                String author = bookCopySet.getString(4);
                String edition = bookCopySet.getString(5);
                String publishingYear = bookCopySet.getString(6);
                Book book = new Book(bookID, title, author, edition, publishingYear, publisher, getQuantityOfBooksByID(bookID));
                String copyID = bookCopySet.getString(7);
                BookCopy copy = new BookCopy(book, copyID);
                bookCopys.add(copy);
            }
        } catch (SQLException ex) {
            bookCopys = null;
        }
        return bookCopys;
    }
    
    /**
     * Returns true if connection is valid, otherwise returns false.
     * @return true if connection is valid, otherwise returns false.
     */
    public boolean isConnectionValid(){
        boolean result = false;
        try{
            result = connection.isValid(0);
        } catch (SQLException SQLEx) {
            result = false;
        }
        return result;
    }
    
}
