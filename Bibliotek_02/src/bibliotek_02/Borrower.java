package bibliotek_02;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Oscar Wika, Vinh Tran
 */
public class Borrower {
    
    private final SimpleStringProperty borrowerId;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName; 
    private final SimpleStringProperty telephone;
 
    Borrower(String borrowerId, String firstName, String lastName, String telephone)
    {
        this.borrowerId = new SimpleStringProperty(borrowerId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.telephone = new SimpleStringProperty(telephone);
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
    
    public String getBorrowerId(){
        return borrowerId.get();
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
