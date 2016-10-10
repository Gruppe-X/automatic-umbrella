package bibliotek_02;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Oscar Wika, Vinh Tran, Robert Rishaug
 */
public class BookCopy {
    
    private final SimpleStringProperty copyID;
    private final Book book;
    
    BookCopy(Book book, String copyID)
    {
        this.book = book;
        this.copyID = new SimpleStringProperty(copyID);
    }
    
    /**
     * Returns id of the book.
     * @return id of the book.
     */
    public String getBookID() {
        return book.getBookID();
    }

    /**
     * Returns name/title of the book.
     * @return name/title of the book.
     */
    public String getBookName() {
        return book.getBookName();
    }

    /**
     * Returns publisher of the book.
     * @return publisher of the book.
     */
    public String getBookPublisher() {
        return book.getBookPublisher();
    }

    /**
     * Returns author of the book.
     * @return author of the book.
     */
    public String getBookAuthor() {
        return book.getBookAuthor();
    }

    /**
     * Returns edition of the book.
     * @return edition of the book.
     */
    public String getBookEdition() {
        return book.getBookEdition();
    }
    
    /**
     * Returns publishing year of the book.
     * @return publishing year of the book.
     */
    public String getBookPublishingYear() {
        return book.getBookYear();
    }

    /**
     * Returns id of the copy.
     * @return id of the copy.
     */
    public String getCopyID() {
        return copyID.get();
    }
    
    /**
     * Returns book object.
     * @return book object.
     */
    public Book getBook() {
        return book;
    }
    
}
