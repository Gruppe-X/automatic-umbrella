package bibliotek_02;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Oscar Wika, Vinh Tran
 */
public class InventoryBook {
    
    private final SimpleStringProperty ID;
    private final SimpleStringProperty title;
    private final SimpleStringProperty edition;
    private final SimpleStringProperty author;
    private final SimpleStringProperty year;
    private final SimpleStringProperty publisher;
    private final SimpleStringProperty quantity;
    private final SimpleStringProperty availableQuantity;
    
    InventoryBook(String ID, String title, String author, String edition, String year, String publisher, String quantity, String availableQuantity)
    {
        this.ID = new SimpleStringProperty(ID);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.edition = new SimpleStringProperty(edition);
        this.year = new SimpleStringProperty(year);
        this.publisher = new SimpleStringProperty(publisher);
        if(quantity == null){
            quantity = "0";
        }
        this.quantity = new SimpleStringProperty(quantity);
        this.availableQuantity = new SimpleStringProperty(availableQuantity);
    }
    
    /**
     * Returns the first name of a customer
     * @return Returns the first name of a customer
     */
    public String getBookID()
    {
        return ID.get();
    }
    
    /**
     * Returns the last name of a customer
     * @return Returns the last name of a customer
     */
    public String getBookName()
    {
       return title.get();
    }
        
    /**
     * 
     * @return 
     */
    public String getBookAuthor()
    {
        return author.get();
    }
    
     /**
     * Returns the telephone of a customer
     * @return Returns the telephone of a customer
     */
    public String getBookEdition()
    {
        return edition.get();
    }
    
    /**
     * 
     * @return 
     */
    public String getBookYear()
    {
        return year.get();
    }
    
    /**
     * 
     * @return 
     */
    public String getBookPublisher()
    {
        return publisher.get();
    }
    
    /**
     * 
     * @param bookID
     */
    public void setBookID(String bookID)
    {
        this.ID.set(bookID);
    }
    
    /**
     * 
     * @param bookName 
     */
    public void setBookName(String bookName)
    {
        this.title.set(bookName);
    }
    
    /**
     * 
     * @param bookAuthor 
     */
    public void setBookAuthor(String bookAuthor)
    {
        this.author.set(bookAuthor);
    }
    
    /**
     * 
     * @param bookEdition 
     */
    public void setBookEdition(String bookEdition)
    {
        this.edition.set(bookEdition);
    }
    
    public void setBookYear(String bookYear)
    {
        this.year.set(bookYear);
    }
    
    public void setBookPublisher(String bookPublisher)
    {
        this.publisher.set(bookPublisher);
    }
    
    public String getBookQuantity() {
        return quantity.get();
    }
    
    public String getBookAvailableQuantity(){
        return availableQuantity.get();
    }
    
}
