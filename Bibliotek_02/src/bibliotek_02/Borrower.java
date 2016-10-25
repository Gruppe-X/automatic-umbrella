package bibliotek_02;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Oscar Wika, Vinh Tran
 */
public class Borrower {
    
    //private final SimpleStringProperty borrowerId;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName; 
    private final SimpleStringProperty telephone;
    private final SimpleIntegerProperty borrowerID;
 

    Borrower(int borrowerID, String firstName, String lastName, String telephone)
    {
        this.borrowerID = new SimpleIntegerProperty(borrowerID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.telephone = new SimpleStringProperty(telephone);
    }
    
    public Borrower(String firstName, String lastName, String telephone)
    {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.telephone = new SimpleStringProperty(telephone);
        this.borrowerID = null;
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
     * Returns the first name of a customer
     * @return Returns the first name of a customer
     */
    public String getFirstName()
    {
        return firstName.get();
    }
    
    /**
     * Returns the last name of a customer
     * @return Returns the last name of a customer
     */
    public String getLastName()
    {
       return lastName.get();
    }
    
     /**
     * Returns the telephone of a customer
     * @return Returns the telephone of a customer
     */
    public String getTelephone()
    {
        return telephone.get();
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
     * @param firstName 
     */
    public void setFirstName(String firstName)
    {
        this.firstName.set(firstName);
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
     * @param telephone 
     */
    public void setTelephone(String telephone)
    {
        this.telephone.set(telephone);
    }
}
