package omarihamza.models;

import java.io.Serializable;

public class AppSettings implements Serializable {

    private String userEmail;
    private boolean rememberUserEmail;
    private String host, port;
    private int whatsAppTimeout;
    private String nameColumn, phoneColumn, emailColumn, apartmentColumn;

    public AppSettings(String userEmail, boolean rememberUserEmail, String host, String port, int whatsAppTimeout) {
        this.userEmail = userEmail;
        this.rememberUserEmail = rememberUserEmail;
        this.host = host;
        this.port = port;
        this.whatsAppTimeout = whatsAppTimeout;
    }

    public AppSettings() {
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public String getPhoneColumn() {
        return phoneColumn;
    }

    public void setPhoneColumn(String phoneColumn) {
        this.phoneColumn = phoneColumn;
    }

    public String getEmailColumn() {
        return emailColumn;
    }

    public void setEmailColumn(String emailColumn) {
        this.emailColumn = emailColumn;
    }

    public String getApartmentColumn() {
        return apartmentColumn;
    }

    public void setApartmentColumn(String apartmentColumn) {
        this.apartmentColumn = apartmentColumn;
    }

    public int getWhatsAppTimeout() {
        return whatsAppTimeout;
    }

    public void setWhatsAppTimeout(int whatsAppTimeout) {
        this.whatsAppTimeout = whatsAppTimeout;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean isRememberUserEmail() {
        return rememberUserEmail;
    }

    public void setRememberUserEmail(boolean rememberUserEmail) {
        this.rememberUserEmail = rememberUserEmail;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
