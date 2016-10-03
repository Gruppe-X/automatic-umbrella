package bibliotek_02;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Oscar Wika, Vinh Tran
 */
public class Borrower {
    
    private final SimpleStringProperty fornavn;
    private final SimpleStringProperty etternavn; 
    private final SimpleStringProperty telefon;
 
    Borrower(String fornavn, String etternavn, String telefon)
    {
        this.fornavn = new SimpleStringProperty(fornavn);
        this.etternavn = new SimpleStringProperty(etternavn);
        this.telefon = new SimpleStringProperty(telefon);
    }

    /**
     * Returns the first name of a customer
     * @return Returns the first name of a customer
     */
    public String getFirstName()
    {
        return fornavn.get();
    }
    
    /**
     * Returns the last name of a customer
     * @return Returns the last name of a customer
     */
    public String getLastName()
    {
       return etternavn.get();
    }
    
     /**
     * Returns the telephone of a customer
     * @return Returns the telephone of a customer
     */
    public String getTelephone()
    {
        return telefon.get();
    }
    
    /**
     * 
     * @param fornavn 
     */
    public void setFirstName(String fornavn)
    {
        this.fornavn.set(fornavn);
    }
    
    /**
     * 
     * @param etternavn 
     */
    public void setLastName(String etternavn)
    {
        this.etternavn.set(etternavn);
    }
    
    /**
     * 
     * @param telefon 
     */
    public void setTelephone(String telefon)
    {
        this.telefon.set(telefon);
    }
}
