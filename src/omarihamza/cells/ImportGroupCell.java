package omarihamza.cells;

import com.jfoenix.controls.JFXCheckBox;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import omarihamza.models.Group;

public class ImportGroupCell extends ListCell<Group> {

    private HBox content;
    private JFXCheckBox checkBox;
    private Text groupTitle;
    private Text groupMembers;

    public ImportGroupCell() {
        super();
        checkBox = new JFXCheckBox();
        groupTitle = new Text();
        groupMembers = new Text();
        VBox vBox = new VBox(groupTitle, groupMembers);
        content = new HBox(checkBox, vBox);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
    }

    @Override
    protected void updateItem(Group item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            groupTitle.setText(item.getTitle());
            groupMembers.setText(String.format("%s", item.getContacts().size() + " Members"));
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> item.setSelected(newValue));
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }

}
