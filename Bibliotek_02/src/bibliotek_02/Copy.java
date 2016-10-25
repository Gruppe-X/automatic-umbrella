package bibliotek_02;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Thomas S Todal
 */
public class Copy {
    
    private final  SimpleIntegerProperty loanID;
    private final  SimpleIntegerProperty borrowerID;
    private final  SimpleIntegerProperty librarianID;
    private final  SimpleStringProperty startDateTime;
    private final  SimpleStringProperty endDateTime;
    private final  SimpleBooleanProperty handedIn;
    
    Copy(int loanID, int borrowerID, int librarianID, String startDateTime, String endDateTime, boolean handedIn)
    {
        this.loanID = new SimpleIntegerProperty(loanID);
        this.borrowerID = new SimpleIntegerProperty(borrowerID);
        this.librarianID = new SimpleIntegerProperty(librarianID);
        this.startDateTime = new SimpleStringProperty(startDateTime);
        this.endDateTime = new SimpleStringProperty(endDateTime);
        this.handedIn = new SimpleBooleanProperty(handedIn);
    }

    /**
     *
     * @return
     */
    public int getLoanID()
    {
        return loanID.get();
    }

    /**
     * 
     * @param loanID 
     */
    public void setLoanID(int loanID)
    {
        this.loanID.set(loanID);
    }

    /**
     * 
     * @return 
     */
    public int getBorrowerID()
    {
        return borrowerID.get();
    }

    /**
     * 
     * @param borrowerID 
     */
    public void setBorrowerID(int borrowerID)
    {
        this.borrowerID.set(borrowerID);
    }

    /**
     * 
     * @return 
     */
    public int getLibrarianID()
    {
        return librarianID.get();
    }

    /**
     * 
     * @param librarianID 
     */
    public void setLibrarianID(int librarianID)
    {
        this.librarianID.set(librarianID);
    }

    public String getStartDateTime()
    {
        return startDateTime.get();
    }

    /**
     * 
     * @param startDateTime 
     */
    public void setStartDateTime(String startDateTime)
    {
        this.startDateTime.set(startDateTime);
    }

    /**
     * 
     * @return 
     */
    public String getEndDateTime()
    {
        return endDateTime.get();
    }

    /**
     * 
     * @param endDateTime 
     */
    public void setEndDateTime(String endDateTime)
    {
        this.endDateTime.set(endDateTime);
    }

    /**
     * 
     * @return 
     */
    public boolean getHandedIn()
    {
        return handedIn.get();
    }

    /**
     * 
     * @param handedIn 
     */
    public void setHandedIn(boolean handedIn)
    {
        this.handedIn.set(handedIn);
    }  
}
