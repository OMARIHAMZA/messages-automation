package omarihamza.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {

    private String title;
    private ArrayList<Contact> contacts;

    public Group(String title, ArrayList<Contact> contacts) {
        this.title = title;
        this.contacts = contacts;
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
}
