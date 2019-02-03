package omarihamza.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import omarihamza.models.Group;

import java.net.URL;
import java.util.ResourceBundle;

public class GroupInfoDialogController implements Initializable {

    private Group group;

    @FXML
    private Text groupTitleTextView;

    @FXML
    private Text groupMembersTextView;

    @FXML
    private JFXListView groupMembersListView;

    @FXML
    private JFXButton addContactButton;

    @FXML
    private JFXButton excelImportButton;

    @FXML
    private JFXButton groupImportButton;


    public GroupInfoDialogController(Group group) {
        this.group = group;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        groupTitleTextView.setText(group.getTitle());
        groupMembersTextView.setText(group.getContacts().size() + " members");

    }


    public void setGroup(Group group) {
        this.group = group;

    }
}
