package omarihamza.cells;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import omarihamza.models.Contact;

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
        VBox vBox = new VBox();
        content = new HBox(name, phone, email);
        content.setSpacing(10);
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