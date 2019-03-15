package omarihamza.models;

import java.io.Serializable;

public class Contact implements Serializable {

    private String name;
    private String phone;
    private String email;
    private String houseNumber;

    public Contact(String name, String phone, String email, String houseNumber) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.houseNumber = houseNumber;
    }

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
}
