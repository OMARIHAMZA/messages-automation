package omarihamza.cells;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import omarihamza.dialogs.ModifyContactController;
import omarihamza.models.Contact;
import omarihamza.utils.FileUtils;
import omarihamza.utils.Utils;

import java.util.ArrayList;

public class ContactListCell extends ListCell<Contact> {

    private HBox content;
    private Text name;
    private Text phone;
    private Text email;
    private Text apartmentNo;

    public ContactListCell() {
        super();
        name = new Text();
        phone = new Text();
        email = new Text();
        apartmentNo = new Text();
        content = new HBox(name, apartmentNo, phone, email);
        content.setSpacing(10);
    }


    @Override
    protected void updateItem(Contact item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            name.setText(item.getName());
            apartmentNo.setText("Apartment No: " + item.getApartmentNumber());
            phone.setText(item.getPhone());
            email.setText(item.getEmail());
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }


}
