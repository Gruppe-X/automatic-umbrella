package bibliotek_02;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Oscar Wika
 */
public class Librarian {
    
    private final SimpleStringProperty employeeID;
    private final SimpleStringProperty employeeFirstName;
    private final SimpleStringProperty employeeLastName;
    private final boolean isAdmin;
    
    public Librarian(String employeeID, String employeeFirstName, String employeeLastName, boolean isAdmin)
    {
        this.employeeID = new SimpleStringProperty(employeeID);
        this.employeeFirstName = new SimpleStringProperty(employeeFirstName);
        this.employeeLastName = new SimpleStringProperty(employeeLastName);
        this.isAdmin = isAdmin;
    }
    
    public Librarian(String employeeID, String employeeFirstName, String employeeLastName)
    {
        this(employeeID, employeeFirstName, employeeLastName, false);
    }
    
    public Librarian(String employeeFirstName, String employeeLastName)
    {
        this(null, employeeFirstName, employeeLastName);
    }
    
    /**
     * Returns the ansattID of an employee.
     * @return Returns the ansattID of an employee.
     */
    public String getEmployeeID()
    {
        return employeeID.get();
    }
    
    /**
     * Returns the first name of an employee.
     * @return Returns the first name of an employee.
     */
    public String getFirstName()
    {
        return employeeFirstName.get();
    }
    
    /**
     * Returns the last name of an employee.
     * @return Returns the last name of an employee.
     */
    public String getLastName()
    {
        return employeeLastName.get();
    }
    
    /**
     * 
     * @param employeeID 
     */
    public void setAnsattID(String employeeID)
    {
        this.employeeID.set(employeeID);
    }
    
    /**
     * 
     * @param employeeFirstName 
     */
    public void setFirstName(String employeeFirstName)
    {
        this.employeeFirstName.set(employeeFirstName);
    }
    
    /**
     * 
     * @param employeeLastName 
     */
    public void setLastName(String employeeLastName)
    {
        this.employeeLastName.set(employeeLastName);
    }
    
    public boolean isAdmin(){
        return isAdmin;
    }
}
