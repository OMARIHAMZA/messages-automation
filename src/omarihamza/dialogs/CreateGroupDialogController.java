package omarihamza.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import omarihamza.cells.ContactListCell;
import omarihamza.models.Contact;
import omarihamza.models.Group;
import omarihamza.utils.FileUtils;
import omarihamza.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static omarihamza.utils.Utils.listContainsContact;

public class CreateGroupDialogController implements Initializable {

    @FXML
    private JFXTextField groupNameTextField;

    @FXML
    private JFXTextArea groupDescriptionTextArea;

    @FXML
    private Text membersText;

    @FXML
    private JFXListView membersListView;

    @FXML
    private JFXButton excelImportButton;

    @FXML
    private JFXButton groupImportButton;

    @FXML
    private JFXButton createGroupButton;

    private ObservableList<Contact> data = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        membersListView.setCellFactory(listView -> new ContactListCell());
        assignActions();

    }

    @SuppressWarnings("ALL")
    private void assignActions() {

        excelImportButton.setOnAction(e -> {
            int counter = Utils.readContactsFromExcelFile(data, excelImportButton.getScene().getWindow());
            if (counter == -1) return;
            membersListView.setItems(data);
            Utils.showPopup("Success", "Successfully added " + counter + " contacts", Alert.AlertType.INFORMATION);
        });

        groupImportButton.setOnAction(e -> {
            FXMLLoader loader = null;
            Parent root = null;
            try {
                loader = new FXMLLoader(getClass().getResource("/omarihamza/layouts/GroupsImportDialog.fxml"));
                root = loader.load();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Select Groups");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            FXMLLoader finalLoader = loader;
            stage.setOnHiding(ee -> {
                GroupsImportDialogController controller = finalLoader.getController();
                if (controller != null) {
                    ArrayList<Group> groups = controller.getSelectedGroups();
                    for (Group group : groups) {
                        for (Contact contact : group.getContacts()) {
                            if (!listContainsContact(data, contact)) {
                                data.add(contact);
                            }
                        }
                    }
                    membersListView.setItems(data);
                }
            });

            stage.showAndWait();

        });

        createGroupButton.setOnAction(e -> {

            if (checkFields()) {
                ArrayList<Contact> contacts = new ArrayList<>(data);
                FileUtils.storeGroup(groupNameTextField.getText(), groupDescriptionTextArea.getText(), contacts);
                ((Stage) createGroupButton.getScene().getWindow()).close();
            }

        });

    }

    private boolean checkFields() {
        return !groupNameTextField.getText().isEmpty();
    }

}