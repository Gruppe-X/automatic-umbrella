package bibliotek_02;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert
 */
public class DatabaseHandler implements Closeable {
    
    private final String connString = "jdbc:sqlserver://roberris-prosjektx.uials.no;databaseName=Bibliotek;username=sa;password=password123";
    private Connection connection;
    private PreparedStatement searchStatement;
    private PreparedStatement bookCopyJoinStatement;
    private PreparedStatement bookCopyWithId;
    private PreparedStatement addBookStatement;
    private PreparedStatement deleteBookStatement;
    private PreparedStatement availableCopyStatement;
    
    public DatabaseHandler() {
        connect();
        try{
            searchStatement = connection.prepareStatement("SELECT * FROM ? WHERE ? = ?");
            bookCopyJoinStatement = connection.prepareStatement("SELECT B.ISBN, Tittel, Forlag, Forfatter, Utgave, Utgivelsesår, E.EksemplarID FROM Bok B RIGHT JOIN Eksemplar E ON B.ISBN = E.ISBN");
            bookCopyWithId = connection.prepareStatement("SELECT B.ISBN, Tittel, Forlag, Forfatter, Utgave, Utgivelsesår, Antall, E.EksemplarID FROM Bok B RIGHT JOIN Eksemplar E ON B.ISBN = E.ISBN WHERE B.ISBN = ?");
            addBookStatement = connection.prepareStatement("INSERT INTO Bok VALUES(?, ?, ?, ?, ?, ?)");
            deleteBookStatement = connection.prepareStatement("DELETE FROM Bok WHERE ISBN = ?");
            availableCopyStatement = connection.prepareStatement("SELECT * FROM Eksemplar WHERE ISBN = ? AND Utlånt = 0");
            
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
    
    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
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
    
    private ResultSet getCopysWithId(String id){
        ResultSet results;
        try {
            bookCopyWithId.setString(1, id);
            results = bookCopyWithId.executeQuery();
        } catch (SQLException ex) {
            results = null;
        }
        return results;
    }
    
    public List<BookCopy> getAvailableCopys(InventoryBook book){
        System.out.println(book.getBookID());
        List<BookCopy> selectedCopys = new ArrayList<>();
        ResultSet results;
        try{
            availableCopyStatement.setString(1, book.getBookID());
            results = availableCopyStatement.executeQuery();
            while(results.next()){
                selectedCopys.add(new BookCopy(book, results.getString(1)));
            }
        } catch (SQLException SQLEx) {
            System.out.println(SQLEx.getMessage());
            SQLEx.printStackTrace();
            selectedCopys = null;
        }
        return selectedCopys;
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
    
    public List<InventoryBook> listBooks(){
        List<InventoryBook> books = new ArrayList<>();
        ResultSet bookSet = getBooks();
        try{
            while(bookSet.next()){
                books.add(new InventoryBook(bookSet.getString(1), bookSet.getString(2), bookSet.getString(4), bookSet.getString(5), bookSet.getString(6), bookSet.getString(3), bookSet.getString(7), bookSet.getString(8)));
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
                String quantity = bookCopySet.getString(7);
                //TODO ????
                InventoryBook book = new InventoryBook(bookID, title, author, edition, publishingYear, publisher, quantity, "0");
                String copyID = bookCopySet.getString(8);
                BookCopy copy = new BookCopy(book, copyID);
                bookCopys.add(copy);
            }
        } catch (SQLException ex) {
            bookCopys = null;
        }
        return bookCopys;
    }
    
    public List<BookCopy> listBookCopysWithId(String id){
        List<BookCopy> bookCopys = new ArrayList<>();
        ResultSet bookCopySet = getCopysWithId(id);
        try {
            while(bookCopySet.next()){
                String bookID = bookCopySet.getString(1);
                String title = bookCopySet.getString(2);
                String publisher = bookCopySet.getString(3);
                String author = bookCopySet.getString(4);
                String edition = bookCopySet.getString(5);
                String publishingYear = bookCopySet.getString(6);
                String quantity = bookCopySet.getString(7);
                String availableQuantity = bookCopySet.getString(8);
                InventoryBook book = new InventoryBook(bookID, title, author, edition, publishingYear, publisher, quantity, availableQuantity);
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
}
