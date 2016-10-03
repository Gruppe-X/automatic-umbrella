package bibliotek_02;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Oscar Wika, Vinh Tran
 */
public class Inventory {
    
    private final SimpleStringProperty bookID;
    private final SimpleStringProperty bookName;
    private final SimpleStringProperty bookQuantity;
   // private final SimpleStringProperty bookAuthor;
    //private final SimpleStringProperty bookYear;
    //private final SimpleStringProperty bookPublisher;
    
    Inventory(String bookID, String bookName, String bookQuantity)
    {
        this.bookID = new SimpleStringProperty(bookID);
        this.bookName = new SimpleStringProperty(bookName);
        this.bookQuantity = new SimpleStringProperty(bookQuantity);
        
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
     * Returns the telephone of a customer
     * @return Returns the telephone of a customer
     */
    public String getBookQuantity()
    {
        return bookQuantity.get();
    }
    
    /*
    public String getBookAuthor()
    {
        return bookAuthor.get();
    }
    */
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
    public void sstBookName(String bookName)
    {
        this.bookName.set(bookName);
    }
    
    /**
     * 
     * @param bookQuantity 
     */
    public void setBookQuantity(String bookQuantity)
    {
        this.bookQuantity.set(bookQuantity);
    }
    
}
