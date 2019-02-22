package omarihamza.cells;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import omarihamza.callbacks.DeleteGroupCallback;
import omarihamza.models.Group;

import javax.sound.sampled.Line;

public class GroupListCell extends ListCell<Group> {


    private HBox content;
    private Text name;
    private Text members;

    public GroupListCell(DeleteGroupCallback deleteGroupCallback) {
        super();
        name = new Text();
        members = new Text();
        VBox vBox = new VBox(name, members);
        content = new HBox(vBox);
        content.setSpacing(10);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteGroup = new MenuItem("Delete Group");
        deleteGroup.setOnAction(e -> deleteGroupCallback.deleteGroup(getIndex()));
        contextMenu.getItems().add(deleteGroup);
        content.setOnContextMenuRequested(event -> {
            contextMenu.show(content.getParent(), event.getScreenX(), event.getScreenY());
        });
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
