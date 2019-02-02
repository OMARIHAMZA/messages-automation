package omarihamza.cells;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import omarihamza.models.Contact;
import omarihamza.models.Group;

public class ContactsListCell extends ListCell<Group> {


    private HBox content;
    private Text name;
    private Text price;

    public ContactsListCell() {
        super();
        name = new Text();
        price = new Text();
        VBox vBox = new VBox(name, price);
        content = new HBox(new Label("[Graphic]"), vBox);
        content.setSpacing(10);
    }

    @Override
    protected void updateItem(Group item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            name.setText(item.getTitle());
            price.setText(String.format("%s", item.getTitle()));
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }

}
