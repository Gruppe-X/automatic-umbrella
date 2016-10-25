package bibliotek_02;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Thomas S Todal
 */
public class Copy {
    
    private final  SimpleStringProperty loanID;
    private final  SimpleStringProperty borrowerID;
    private final  SimpleStringProperty librarianID;
    private final  SimpleStringProperty startDateTime;
    private final  SimpleStringProperty endDateTime;
    private final  SimpleStringProperty handedIn;
    
    Copy(String loanID, String borrowerID, String librarianID, String startDateTime, String endDateTime, String handedIn)
    {
        this.loanID = new SimpleStringProperty(loanID);
        this.borrowerID = new SimpleStringProperty(borrowerID);
        this.librarianID = new SimpleStringProperty(librarianID);
        this.startDateTime = new SimpleStringProperty(startDateTime);
        this.endDateTime = new SimpleStringProperty(endDateTime);
        this.handedIn = new SimpleStringProperty(handedIn);
    }

    /**
     *
     * @return
     */
    public String getLoanID()
    {
        return loanID.get();
    }

    /**
     * 
     * @param loanID 
     */
    public void setLoanID(String loanID)
    {
        this.loanID.set(loanID);
    }

    /**
     * 
     * @return 
     */
    public String getBorrowerID()
    {
        return borrowerID.get();
    }

    /**
     * 
     * @param borrowerID 
     */
    public void setBorrowerID(String borrowerID)
    {
        this.borrowerID.set(borrowerID);
    }

    /**
     * 
     * @return 
     */
    public String getLibrarianID()
    {
        return librarianID.get();
    }

    /**
     * 
     * @param librarianID 
     */
    public void setLibrarianID(String librarianID)
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
    public String getHandedIn()
    {
        return handedIn.get();
    }

    /**
     * 
     * @param handedIn 
     */
    public void setHandedIn(String handedIn)
    {
        this.handedIn.set(handedIn);
    }  
}
