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
import omarihamza.models.Contact;
import omarihamza.utils.FileUtils;

import java.util.ArrayList;

public class ContactListCell extends ListCell<Contact> {

    private HBox content;
    private Text name;
    private Text phone;
    private Text email;

    public ContactListCell() {
        super();
        name = new Text();
        phone = new Text();
        email = new Text();
        content = new HBox(name, phone, email);
        content.setSpacing(10);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e -> {

        });
        contextMenu.getItems().add(delete);
        content.setOnContextMenuRequested(event -> {
            contextMenu.show(content.getParent(), event.getScreenX(), event.getScreenY());
        });
    }



    @Override
    protected void updateItem(Contact item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            name.setText(item.getName());
            phone.setText(item.getPhone());
            email.setText(item.getEmail());
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }



}
