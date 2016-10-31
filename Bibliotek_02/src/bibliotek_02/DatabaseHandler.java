package bibliotek_02;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Robert, Oscar
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
    private PreparedStatement registerLoanStatement;
    private PreparedStatement registerCopyToLoanStatement;
    private PreparedStatement overdueLoansStatement;
    
    private PreparedStatement addBorrowerStatement;
    private PreparedStatement deleteBorrowerStatement;
    
    private PreparedStatement addLibrarianStatement;
    private PreparedStatement deleteLibrarianStatement;
    private PreparedStatement getLibrarianByIdStatement;
    
    public DatabaseHandler() {
        connect();
        try{
            searchStatement = connection.prepareStatement("SELECT * FROM ? WHERE ? = ?");
            bookCopyJoinStatement = connection.prepareStatement("SELECT B.ISBN, Tittel, Forlag, Forfatter, Utgave, Utgivelsesår, E.EksemplarID, E.Utlånt FROM Bok B RIGHT JOIN Eksemplar E ON B.ISBN = E.ISBN");
            bookCopyWithId = connection.prepareStatement("SELECT B.ISBN, Tittel, Forlag, Forfatter, Utgave, Utgivelsesår, Antall, E.EksemplarID, E.Utlånt FROM Bok B RIGHT JOIN Eksemplar E ON B.ISBN = E.ISBN WHERE B.ISBN = ?");
            addBookStatement = connection.prepareStatement("INSERT INTO Bok VALUES(?, ?, ?, ?, ?, ?, NULL, NULL)");
            deleteBookStatement = connection.prepareStatement("DELETE FROM Bok WHERE ISBN = ?");
            availableCopyStatement = connection.prepareStatement("SELECT * FROM Eksemplar WHERE ISBN = ? AND Utlånt = 0");
            //1=BorrowerID, 2=LibrarianID/EmployeeID, 2=Number of days to loan
            registerLoanStatement = connection.prepareStatement("INSERT INTO Lån (LånetakerID, AnsattID, Starttidspunkt, Slutttidspunkt) VALUES(?, ?, GETDATE(), DATEADD(day, ?, GETDATE()));", Statement.RETURN_GENERATED_KEYS);
            registerCopyToLoanStatement = connection.prepareStatement("UPDATE Eksemplar SET LånId = ?, Utlånt=1 WHERE EksemplarID = ?");
            overdueLoansStatement = connection.prepareStatement("EXEC dbo.overDueProcedure");
            
            addBorrowerStatement = connection.prepareStatement("INSERT INTO Lånetaker VALUES(?, ?, ?)");
            deleteBorrowerStatement = connection.prepareStatement("DELETE FROM Lånetaker WHERE LånetakerID = ?");
            
            addLibrarianStatement = connection.prepareStatement("INSERT INTO Ansatt VALUES(?, ?)");
            deleteLibrarianStatement = connection.prepareStatement("DELETE FROM Ansatt WHERE AnsattID = ?");
            getLibrarianByIdStatement = connection.prepareStatement("SELECT * FROM Ansatt WHERE AnsattID = ?");
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
    
    public ResultSet getReceipts(){
        return getResultSet("SELECT LånID, L.LånetakerID, AnsattID, Starttidspunkt, Slutttidspunkt, Levert, Fornavn, Etternavn, Telefon FROM Lån L RIGHT JOIN Lånetaker T ON L.LånetakerID = T.LånetakerID");
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
    
    /**
     * 
     * @return 
     */
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
     * 
     * @param id
     * @return 
     */
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
                selectedCopys.add(new BookCopy(book, results.getString(1), true));
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
                customers.add(new Borrower(customerSet.getInt(1), customerSet.getString(2), customerSet.getString(3), customerSet.getString(4)));
            }
        } catch (SQLException SQLEx) {
            //TODO
        }
        return customers;
    }
    
    /**
     * Returns a list of all the librarians.
     * @return Returns a list of all the librarians.
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
    
    /**
     * Returns librarian object of librarian found in database with given id
     * @param id id of librarian to find
     * @return librarian object of data in database. If none where found returns null.
     */
    public Librarian getLibrarianById(String id){
        Librarian librarian;
        try {
            getLibrarianByIdStatement.setString(1, id);
            ResultSet librarianSet = getLibrarianByIdStatement.executeQuery();
            librarianSet.next();
            librarian = new Librarian(librarianSet.getString(1), librarianSet.getString(2), librarianSet.getString(3));
        } catch (SQLException ex) {
            librarian = null;
        }
        return librarian;
    }
    
    
    /**
     * Returns a list of all the books 
     * @return Returns a list of all the books
     */
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
    
    public List<Copy> listCopies() {
        List<Copy> copies = new ArrayList<>();
        ResultSet copySet = getReceipts();
        
        try {
            while (copySet.next()) {
                copies.add(new Copy(copySet.getString(1), copySet.getString(2), copySet.getString(3), copySet.getString(4), copySet.getString(5), copySet.getString(6), copySet.getString(7), copySet.getString(8), copySet.getString(9)));
            }
        } catch ( SQLException ex) {
                //TODO
                System.out.println("looooooool");
        }
        return copies;
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
                //TODO ????
                InventoryBook book = new InventoryBook(bookID, title, author, edition, publishingYear, publisher, quantity, "0");
                String copyID = bookCopySet.getString(8);
                int avaiableBit = bookCopySet.getInt(9);
                boolean available;
                if(avaiableBit>0){
                    available = false;
                } else {
                    available = true;
                }
                BookCopy copy = new BookCopy(book, copyID, available);
                bookCopys.add(copy);
            }
        } catch (SQLException ex) {
            bookCopys = null;
        }
        return bookCopys;
    }
    
    /**
     * 
     * @param id
     * @return 
     */
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
                int avaiableBit = bookCopySet.getInt(9);
                boolean available;
                if(avaiableBit>0){
                    available = false;
                } else {
                    available = true;
                }
                BookCopy copy = new BookCopy(book, copyID, available);
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
            addLibrarianStatement.setString(1, newLibrarian.getFirstName());
            addLibrarianStatement.setString(2, newLibrarian.getLastName());
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
    /**
     * Returns true if 
     * @param bookToDelete The book to be deleted
     * @return 
     */
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

    
    public boolean registerLoan(int borrowerId, int librarianId, int numberOfDays, List<BookCopy> copysToLoan){
        boolean success = false;
        ResultSet result;
        int affectedRows=0;
        try {
            connection.setAutoCommit(false);
            registerLoanStatement.setInt(1, borrowerId);
            registerLoanStatement.setInt(2, librarianId);
            registerLoanStatement.setInt(3, numberOfDays);
            affectedRows = registerLoanStatement.executeUpdate();
            if (affectedRows > 0) {
                result = registerLoanStatement.getGeneratedKeys();
                result.next();
                int loanId = result.getInt(1);
                Iterator<BookCopy> copysIt = copysToLoan.iterator();
                while (copysIt.hasNext()) {
                    BookCopy currentCopy = copysIt.next();
                    registerCopyToLoanStatement.setInt(1, loanId);
                    registerCopyToLoanStatement.setString(2, currentCopy.getCopyID());
                    registerCopyToLoanStatement.executeUpdate();
                }
                success = true;
                connection.commit();
            } else {
                success = false;
            }
        } catch (SQLException ex) {
            success = false;
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try{
                connection.setAutoCommit(true);
            } catch (SQLException SQLEx){
                
            }
        }
        return success;
    }
    

    //TODO fix nullpointer
    public boolean deleteBorrower(Borrower borrowerToDelete){
        boolean result = false;
        try {
            deleteBorrowerStatement.setInt(1, borrowerToDelete.getBorrowerID());
            if(deleteBorrowerStatement.executeUpdate() > 0){
                result = true;
            }
        } catch (SQLException ex) {
            result = false;
        }
        return result;
    }
    
    /**
     * Returns true if 
     * @param librarianToDelete
     * @return 
     */
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
