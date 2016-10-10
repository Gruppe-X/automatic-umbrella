package bibliotek_02;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Oscar Wika, Vinh Tran, Robert Rishaug
 */
public class BookCopy {
    
    private final SimpleStringProperty bookID;
    private final SimpleStringProperty bookName;
    private final SimpleStringProperty bookPublisher;
    private final SimpleStringProperty bookAuthor;
    private final SimpleStringProperty bookEdition;
    private final SimpleStringProperty bookPublishingYear;
    private final SimpleStringProperty copyID;
    private final Book book;
    //private final SimpleStringProperty bookAuthor;
    //private final SimpleStringProperty bookYear;
    //private final SimpleStringProperty bookPublisher;
    
    BookCopy(Book book, String copyID)
    {
        this.book = book;
        this.bookID = new SimpleStringProperty(book.getBookID());
        this.bookName = new SimpleStringProperty(book.getBookName());
        this.bookPublisher = new SimpleStringProperty(book.getBookPublisher());
        this.bookAuthor = new SimpleStringProperty(book.getBookAuthor());
        this.bookEdition = new SimpleStringProperty(book.getBookEdition());
        this.bookPublishingYear = new SimpleStringProperty(book.getBookYear());
        this.copyID = new SimpleStringProperty(copyID);
    }
    
    /**
     * Returns id of the book.
     * @return id of the book.
     */
    public SimpleStringProperty getBookID() {
        return bookID;
    }

    /**
     * Returns name/title of the book.
     * @return name/title of the book.
     */
    public SimpleStringProperty getBookName() {
        return bookName;
    }

    /**
     * Returns publisher of the book.
     * @return publisher of the book.
     */
    public SimpleStringProperty getBookPublisher() {
        return bookPublisher;
    }

    /**
     * Returns author of the book.
     * @return author of the book.
     */
    public SimpleStringProperty getBookAuthor() {
        return bookAuthor;
    }

    /**
     * Returns edition of the book.
     * @return edition of the book.
     */
    public SimpleStringProperty getBookEdition() {
        return bookEdition;
    }
    
    /**
     * Returns publishing year of the book.
     * @return publishing year of the book.
     */
    public SimpleStringProperty getBookPublishingYear() {
        return bookPublishingYear;
    }

    /**
     * Returns id of the copy.
     * @return id of the copy.
     */
    public SimpleStringProperty getCopyID() {
        return copyID;
    }
    
    /**
     * Returns book object.
     * @return book object.
     */
    public Book getBook() {
        return book;
    }
    
}
