package omarihamza.models;


import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {

    private String title;
    private String description;
    private ArrayList<Contact> contacts;
    private boolean isSelected;


    public Group(String title, String description, ArrayList<Contact> contacts) {
        this.title = title;
        this.description = description;
        this.contacts = contacts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
