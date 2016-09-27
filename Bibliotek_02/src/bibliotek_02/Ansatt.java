package bibliotek_02;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Oscar Wika
 */
public class Ansatt {
    
    private final SimpleStringProperty ansattID;
    private final SimpleStringProperty fornavn;
    private final SimpleStringProperty etternavn;
    
    Ansatt(String ansattID, String fornavn, String etternavn)
    {
        this.ansattID = new SimpleStringProperty(ansattID);
        this.fornavn = new SimpleStringProperty(fornavn);
        this.etternavn = new SimpleStringProperty(etternavn);
    }
    
    /**
     * Returns the ansattID of an employee.
     * @return Returns the ansattID of an employee.
     */
    public String getAnsattID()
    {
        return ansattID.get();
    }
    
    /**
     * Returns the first name of an employee.
     * @return Returns the first name of an employee.
     */
    public String getFirstName()
    {
        return fornavn.get();
    }
    
    /**
     * Returns the last name of an employee.
     * @return Returns the last name of an employee.
     */
    public String getLastName()
    {
        return etternavn.get();
    }
    
    /**
     * 
     * @param ansattID 
     */
    public void setAnsattID(String ansattID)
    {
        this.ansattID.set(ansattID);
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
}
