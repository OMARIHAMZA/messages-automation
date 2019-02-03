package omarihamza.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import omarihamza.cells.ImportGroupCell;
import omarihamza.models.Group;
import omarihamza.utils.FileUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GroupsImportDialogController implements Initializable {

    @FXML
    private Text selectGroupsText;

    @FXML
    private JFXListView groupsListView;

    @FXML
    private JFXButton importButton;

    private ObservableList<Group> groups = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        groups.addAll(FileUtils.loadGroups());
        groupsListView.setCellFactory(listView -> new ImportGroupCell());
        groupsListView.setItems(groups);
        importButton.setOnAction(e -> ((Stage) importButton.getScene().getWindow()).close());


    }

    public ArrayList<Group> getSelectedGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        for (Object group : groupsListView.getItems()) {
            if (((Group) group).isSelected()) {
                groups.add(((Group) group));
            }
        }
        return groups;
    }
}
