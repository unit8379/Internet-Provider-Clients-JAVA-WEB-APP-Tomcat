package JavaClasses;

import java.io.Serializable;
 
public class AccountBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String firstName;
    private String lastName;
    private String tariffName;
    private String price;

    public AccountBean(){ }

    public AccountBean(String id, String firstName, String lastName, String tariffName, String price) {
         
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tariffName = tariffName;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTariffName() {
        return tariffName;
    }

    public String getPrice() {
        return price;
    }
}