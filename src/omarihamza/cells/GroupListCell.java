package omarihamza.cells;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import omarihamza.models.Group;

public class GroupListCell extends ListCell<Group> {


    private HBox content;
    private Text name;
    private Text members;

    public GroupListCell() {
        super();
        name = new Text();
        members = new Text();
        VBox vBox = new VBox(name, members);
        content = new HBox(vBox);
        content.setSpacing(10);
    }

    @Override
    protected void updateItem(Group item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            name.setText(item.getTitle());
            members.setText(String.format("%s", item.getContacts().size() + " Members"));
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }

}
