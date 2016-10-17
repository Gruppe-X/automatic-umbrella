package bibliotek_02;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Oscar Wika
 */
public class Copy {
    
    private final SimpleStringProperty bookID;
    private final SimpleStringProperty bookName;
    private final SimpleStringProperty bookEdition;
    private final SimpleStringProperty bookAuthor;
    private final SimpleStringProperty bookYear;
    private final SimpleStringProperty bookPublisher;
    
    Copy(String bookID, String bookName, String bookAuthor, String bookEdition, String bookYear, String bookPublisher)
    {
        this.bookID = new SimpleStringProperty(bookID);
        this.bookName = new SimpleStringProperty(bookName);
        this.bookAuthor = new SimpleStringProperty(bookAuthor);
        this.bookEdition = new SimpleStringProperty(bookEdition);
        this.bookYear = new SimpleStringProperty(bookYear);
        this.bookPublisher = new SimpleStringProperty(bookPublisher);
    }
    
    /**
     * Returns the first name of a customer
     * @return Returns the first name of a customer
     */
    public String getBookID()
    {
        return bookID.get();
    }
    
    /**
     * Returns the last name of a customer
     * @return Returns the last name of a customer
     */
    public String getBookName()
    {
       return bookName.get();
    }
        
    /**
     * 
     * @return 
     */
    public String getBookAuthor()
    {
        return bookAuthor.get();
    }
    
     /**
     * Returns the telephone of a customer
     * @return Returns the telephone of a customer
     */
    public String getBookEdition()
    {
        return bookEdition.get();
    }
    
    /**
     * 
     * @return 
     */
    public String getBookYear()
    {
        return bookYear.get();
    }
    
    /**
     * 
     * @return 
     */
    public String getBookPublisher()
    {
        return bookPublisher.get();
    }
    
    /**
     * 
     * @param bookID
     */
    public void setBookID(String bookID)
    {
        this.bookID.set(bookID);
    }
    
    /**
     * 
     * @param bookName 
     */
    public void setBookName(String bookName)
    {
        this.bookName.set(bookName);
    }
    
    /**
     * 
     * @param bookAuthor 
     */
    public void setBookAuthor(String bookAuthor)
    {
        this.bookAuthor.set(bookAuthor);
    }
    
    /**
     * 
     * @param bookEdition 
     */
    public void setBookEdition(String bookEdition)
    {
        this.bookEdition.set(bookEdition);
    }
    
    public void setBookYear(String bookYear)
    {
        this.bookYear.set(bookYear);
    }
    
    public void setBookPublisher(String bookPublisher)
    {
        this.bookPublisher.set(bookPublisher);
    }
    
}
