package omarihamza.models;

import java.io.Serializable;

public class Contact implements Serializable {

    private String name;
    private String phone;
    private String email;
    private String apartmentNumber;

    public Contact(String name, String phone, String email, String apartmentNumber) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.apartmentNumber = apartmentNumber;
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

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
}
