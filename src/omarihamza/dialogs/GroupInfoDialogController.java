package omarihamza.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import omarihamza.cells.ContactListCell;
import omarihamza.models.Contact;
import omarihamza.models.Group;
import omarihamza.utils.FileUtils;
import omarihamza.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    private ObservableList<Contact> contacts = FXCollections.observableArrayList();


    public GroupInfoDialogController(Group group) {
        this.group = group;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        groupTitleTextView.setText(group.getTitle());
        groupMembersTextView.setText(group.getContacts().size() + " members");
        contacts.addAll(group.getContacts());
        groupMembersListView.setCellFactory(new Callback<ListView<Contact>, ListCell<Contact>>() {
            @Override
            public ListCell<Contact> call(ListView<Contact> param) {
                return new ListCell<Contact>() {

                    @Override
                    protected void updateItem(Contact item, boolean empty) {
                        super.updateItem(item, empty);
                        Text name = new Text();
                        Text phone = new Text();
                        Text email = new Text();
                        HBox content = new HBox(name, phone, email);
                        content.setSpacing(10);

                        ContextMenu contextMenu = new ContextMenu();
                        MenuItem delete = new MenuItem("Delete");
                        delete.setOnAction(e -> {
                            if (!getListView().getItems().isEmpty())
                                deleteContact(getIndex());
                        });
                        contextMenu.getItems().add(delete);
                        content.setOnContextMenuRequested(event -> {
                            contextMenu.show(content.getParent(), event.getScreenX(), event.getScreenY());
                        });

                        if (item != null && !empty) { // <== test for null item and empty parameter
                            name.setText(item.getName());
                            phone.setText(item.getPhone());
                            email.setText(item.getEmail());
                            setGraphic(content);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
//        groupMembersListView.setCellFactory(listView -> new ContactListCell());
        groupMembersListView.setItems(contacts);

        assignActions();

    }

    private void deleteContact(int index) {

        groupMembersListView.getItems().remove(index);
        group.getContacts().remove(index);
        FileUtils.updateGroup(group);
        groupMembersTextView.setText(group.getContacts().size() + " members");

    }

    private void assignActions() {
        groupImportButton.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/omarihamza/layouts/GroupsImportDialog.fxml"));
            Utils.createDialog(loader, "Select Groups", ee -> {
                GroupsImportDialogController controller = loader.getController();
                if (controller != null) {
                    ArrayList<Group> groups = controller.getSelectedGroups();
                    for (Group group : groups) {
                        for (Contact contact : group.getContacts()) {
                            if (!containsContact(contacts, contact)) {
                                contacts.add(contact);
                            }
                        }
                    }
                    ArrayList<Contact> mContacts = new ArrayList<>(contacts);
                    group.setContacts(mContacts);
                    FileUtils.updateGroup(group);
                    groupMembersTextView.setText(contacts.size() + " members");
                    groupMembersListView.setItems(contacts);
                }
            });
        });

        addContactButton.setOnAction(e -> {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/omarihamza/layouts/CreateContactDialog.fxml"));
            Utils.createDialog(fxmlLoader, "Add Contact", ee -> {
                CreateContactDialogController controller = fxmlLoader.getController();
                Contact contact = controller.getContact();
                if (contact == null) return;
                contacts.add(contact);
                group.getContacts().add(contact);
                FileUtils.updateGroup(group);
                groupMembersListView.setItems(contacts);
                groupMembersTextView.setText(contacts.size() + " members");
            });

        });

    }

    public void setGroup(Group group) {
        this.group = group;

    }

    private boolean containsContact(ObservableList<Contact> contacts, Contact mContact) {
        for (Contact contact : contacts) {
            if (contact.getEmail().equals(mContact.getEmail())
                    && contact.getPhone().equals(mContact.getPhone())) {
                return true;
            }
        }
        return false;
    }
}
