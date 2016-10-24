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
    private PreparedStatement addBookStatement;
    private PreparedStatement deleteBookStatement;
    
    private PreparedStatement addBorrowerStatement;
    private PreparedStatement deleteBorrowerStatement;
    
    private PreparedStatement addLibrarianStatement;
    private PreparedStatement deleteLibrarianStatement;
    
    public DatabaseHandler() {
        connect();
        try{
            searchStatement = connection.prepareStatement("SELECT * FROM ? WHERE ? = ?");
            bookCopyJoinStatement = connection.prepareStatement("SELECT B.ISBN, Tittel, Forlag, Forfatter, Utgave, Utgivelsesår, E.EksemplarID FROM Bok B RIGHT JOIN Eksemplar E ON B.ISBN = E.ISBN");
            addBookStatement = connection.prepareStatement("INSERT INTO Bok VALUES(?, ?, ?, ?, ?, ?)");
            deleteBookStatement = connection.prepareStatement("DELETE FROM Bok WHERE ISBN = ?");
            
            addBorrowerStatement = connection.prepareStatement("INSERT INTO Lånetaker VALUES(?, ?, ?)");
            deleteBorrowerStatement = connection.prepareStatement("DELETE FROM Lånetaker WHERE Fornavn = ?");
            
            addLibrarianStatement = connection.prepareStatement("INSERT INTO Ansatt VALUES(?, ?, ?)");
            deleteLibrarianStatement = connection.prepareStatement("DELETE FROM Ansatt WHERE AnsattID = ?");
            
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
    
    public ResultSet getBorrowers(){
        return getResultSet("SELECT * FROM Lånetaker");
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
    
    /**
     * 
     * @return 
     */
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
    

    public List<InventoryBook> listBooks(){
        List<InventoryBook> books = new ArrayList<>();
        ResultSet bookSet = getBooks();
        try{
            while(bookSet.next()){
                books.add(new InventoryBook(bookSet.getString(1), bookSet.getString(2), bookSet.getString(4), bookSet.getString(5), bookSet.getString(6), bookSet.getString(3), bookSet.getString(7)));
            }
        } catch (SQLException ex) {
            books = null;
        }
        return books;
    }
    
    /**
     * 
     * @return 
     */
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
                String quantity = bookCopySet.getString(7);
                InventoryBook book = new InventoryBook(bookID, title, author, edition, publishingYear, publisher, quantity);
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
    
    /*
    /**
     * Adds a book to the book table of the database. ISBN and Title is needed to
     * add book to database.
     * @param newBook Copy object to add to database. 
     * @return true if book was successfully added, otherwise false.
     */
    public boolean addBook(InventoryBook newBook){
        boolean result = false;
        try {
            addBookStatement.setString(1, newBook.getBookID());
            addBookStatement.setString(2, newBook.getBookName());
            addBookStatement.setString(3, newBook.getBookPublisher());
            addBookStatement.setString(4, newBook.getBookAuthor());
            addBookStatement.setInt(5, Integer.parseInt(newBook.getBookEdition()));
            addBookStatement.setInt(6, Integer.parseInt(newBook.getBookYear()));
            int rowsUpdated = addBookStatement.executeUpdate();
            
            if(rowsUpdated > 0){
                
                result = true;
            }
        } catch (SQLException ex) {
            result = false;
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return result;
    }
    
    /**
     * Adds a borrower to the borrowers table of the database. 
     * @param newBorrower
     * @return true if borrower was successfully added, otherwise false.
     */
    public boolean addBorrower(Borrower newBorrower){
        boolean result = false;
        try {
            addBorrowerStatement.setString(1, newBorrower.getFirstName());
            addBorrowerStatement.setString(2, newBorrower.getLastName());
            addBorrowerStatement.setString(3, newBorrower.getTelephone());
            int rowsUpdated = addBorrowerStatement.executeUpdate();
            
            if(rowsUpdated > 0){
                
                result = true;
            }
        } catch (SQLException ex) {
            result = false;
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return result;
    }
    /**
     * Adds a borrower to the borrowers table of the database.
     * @param newLibrarian
     * @return true if borrower was successfully added, otherwise false.
     */
    public boolean addLibrarian(Librarian newLibrarian){
        boolean result = false;
        try {
            addLibrarianStatement.setString(1, newLibrarian.getEmployeeID());
            addLibrarianStatement.setString(2, newLibrarian.getFirstName());
            addLibrarianStatement.setString(3, newLibrarian.getLastName());
            int rowsUpdated = addLibrarianStatement.executeUpdate();
            
            if(rowsUpdated > 0){
                
                result = true;
            }
        } catch (SQLException ex) {
            result = false;
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return result;
    }
    
    //TODO fix nullpointer
    public boolean deleteBook(InventoryBook bookToDelete){
        boolean result = false;
        try {
            deleteBookStatement.setString(1, bookToDelete.getBookID());
            if(deleteBookStatement.executeUpdate() > 0){
                result = true;
            }
        } catch (SQLException ex) {
            result = false;
        }
        return result;
    }
    
    //TODO fix nullpointer
    public boolean deleteBorrower(Borrower borrowerToDelete){
        boolean result = false;
        try {
            deleteBorrowerStatement.setString(1, borrowerToDelete.getFirstName());
            if(deleteBorrowerStatement.executeUpdate() > 0){
                result = true;
            }
        } catch (SQLException ex) {
            result = false;
        }
        return result;
    }
    
    //TODO fix nullpointer
    public boolean deleteLibrarian(Librarian librarianToDelete){
        boolean result = false;
        try {
            deleteLibrarianStatement.setString(1, librarianToDelete.getEmployeeID());
            if(deleteLibrarianStatement.executeUpdate() > 0){
                result = true;
            }
        } catch (SQLException ex) {
            result = false;
        }
        return result;
    }
}
