package bibliotek_02;

import java.sql.Timestamp;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Thomas S Todal
 */
public class Copy {
    
    private final SimpleStringProperty loanID;
    private final SimpleStringProperty borrowerID;
    private final SimpleStringProperty librarianID;
    private final Timestamp startDateTimestamp;
    private final Timestamp endDateTimestamp;
    private final SimpleBooleanProperty handedIn;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty phoneNum;
    private final SimpleStringProperty startDateTime;
    private final SimpleStringProperty endDateTime;
    
    Copy(String loanID, String borrowerID, String librarianID, Timestamp startDateTime, Timestamp endDateTime, Boolean handedIn, String firstName, String lastName, String phoneNum)
    {
        this.loanID = new SimpleStringProperty(loanID);
        this.borrowerID = new SimpleStringProperty(borrowerID);
        this.librarianID = new SimpleStringProperty(librarianID);
        this.startDateTimestamp = startDateTime;
        this.endDateTimestamp = endDateTime;
        this.handedIn = new SimpleBooleanProperty(handedIn);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.phoneNum = new SimpleStringProperty(phoneNum);
        this.startDateTime = new SimpleStringProperty(startDateTimestamp.toString());
        this.endDateTime = new SimpleStringProperty(endDateTimestamp.toString());
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
        return Boolean.toString(handedIn.get());
    }

    /**
     * 
     * @param handedIn 
     */
    public void setHandedIn(boolean handedIn)
    {
        this.handedIn.set(handedIn);
    }
    
    /**
     * 
     * @return 
     */
    public String getFirstName()
    {
        return firstName.get();
    }
    
    /**
     * 
     * @param firstName 
     */
    public void setFirstName(String firstName)
    {
        this.firstName.set(firstName);
    }
    
    /**
     * 
     * @return 
     */
    public String getLastName()
    {
        return lastName.get();
    }
    
    /**
     * 
     * @param lastName 
     */
    public void setLastName(String lastName)
    {
        this.lastName.set(lastName);
    }
    
    /**
     * 
     * @return 
     */
    public String getPhoneNum()
    {
        return phoneNum.get();
    }
    
    /**
     * 
     * @param phoneNum 
     */
    public void setPhoneNum(String phoneNum)
    {
        this.phoneNum.set(phoneNum);
    }
    
    /**
     * 
     * @return 
     */
    public String getLastFirstName()
    {
        String name = lastName.get();
        name += ", ";
        name += firstName.get();
        
        return name;
    }
    
    /**
     * 
     * @return 
     */
    public String getDaysLeft()
    {
        String returnString;
        long timeLeft;
        
        timeLeft = (endDateTimestamp.getTime() - System.currentTimeMillis());
        timeLeft = timeLeft/(24*60*60*1000);
        timeLeft = timeLeft-(timeLeft%1);
        
        if (timeLeft < 0) {
            returnString = "Over tiden";
        } else {
        returnString = Long.toString(timeLeft);
        }
        
        return returnString;
    }
    
    /**
     * 
     * @return 
     */
    public String getAllGood()
    {
        String returnString = "";
        
        if (handedIn.get()) {
            returnString = "Ja";
        } else {
            returnString = "Nei";
        }
        
        return returnString;
    }
}
