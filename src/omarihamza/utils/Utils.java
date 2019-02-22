package omarihamza.utils;

import com.creditdatamw.zerocell.Reader;
import com.creditdatamw.zerocell.column.ColumnInfo;
import com.sun.istack.internal.Nullable;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import omarihamza.models.AppSettings;
import omarihamza.models.Contact;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Utils {

    public static Scene createDialog(FXMLLoader loader, String dialogTitle, @Nullable EventHandler onHidingHandler) {
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle(dialogTitle);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setOnHiding(onHidingHandler);
        stage.showAndWait();
        return scene;
    }

    public static void showPopup(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Optional<ButtonType> showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        return alert.showAndWait();
    }

    private static ArrayList<Contact> parseExcelFile(File file) {

        AppSettings appSettings = FileUtils.loadSettings();

        if (appSettings == null
                || appSettings.getNameColumn() == null
                || appSettings.getEmailColumn() == null
                || appSettings.getPhoneColumn() == null
                || appSettings.getApartmentColumn() == null) {

            Utils.showPopup("Error", "Please specify the title of each column in the Application Settings and try again.", Alert.AlertType.ERROR);

            return new ArrayList<>();
        }

        try {
            List<Contact> contacts = Reader.of(Contact.class)
                    .from(file)
                    .using(
                            new ColumnInfo(appSettings.getNameColumn()/*"الاسم"*/, "name", 0, String.class),
                            new ColumnInfo(/*"رقم الهاتف"*/appSettings.getPhoneColumn(), "phone", 2, String.class),
                            new ColumnInfo(/*"الأيميل"*/appSettings.getEmailColumn(), "email", 1, String.class),
                            new ColumnInfo(/*"رقم الشقة"*/appSettings.getApartmentColumn(), "apartmentNumber", 3, String.class)
                    )
                    .sheet("Sheet1")
                    .list();
            return new ArrayList<>(contacts);
        } catch (Exception e) {
            Utils.showPopup("Error", e.getMessage(), Alert.AlertType.ERROR);
            return new ArrayList<>();
        }
    }

    public static boolean listContainsContact(ObservableList<Contact> contacts, Contact mContact) {
        try {
            for (Contact contact : contacts) {
                if (contact.getPhone().equals(mContact.getPhone())) {
                    return true;
                }
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    public static int readContactsFromExcelFile(ObservableList<Contact> contacts, Window window) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions =
                new FileChooser.ExtensionFilter(
                        "Excel File", "*.xls", "*.xlsx", "*.xla", "*.xlam");
        fileChooser.getExtensionFilters().add(fileExtensions);
        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile == null) return -1;
        if (!selectedFile.isFile()) {
            Utils.showPopup("Error", "Please select a valid Excel File", Alert.AlertType.ERROR);
            return -1;
        }

        ArrayList<Contact> importedContacts = Utils.parseExcelFile(selectedFile);
        int counter = 0;
        for (Contact contact : importedContacts) {
            if (!listContainsContact(contacts, contact)) {
                contacts.add(contact);
                counter++;
            }
        }

        return counter;
    }

}
