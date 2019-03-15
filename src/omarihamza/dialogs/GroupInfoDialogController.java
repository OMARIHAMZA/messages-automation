package omarihamza.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import omarihamza.models.Contact;
import omarihamza.models.Group;
import omarihamza.utils.FileUtils;
import omarihamza.utils.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static omarihamza.utils.Utils.listContainsContact;

public class GroupInfoDialogController implements Initializable {

    private Group group;

    @FXML
    private JFXTextField groupTitleTextView;

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
                        Text apartmentNo = new Text();
                        Text phone = new Text();
                        Text email = new Text();
                        HBox content = new HBox(name, apartmentNo, phone, email);
                        content.setSpacing(10);

                        ContextMenu contextMenu = new ContextMenu();
                        MenuItem modify = new MenuItem("Modify");
                        MenuItem delete = new MenuItem("Delete");
                        delete.setOnAction(e -> {
                            if (!getListView().getItems().isEmpty())
                                deleteContact(getIndex());
                        });
                        modify.setOnAction(e -> {
                            if (getListView().getItems().isEmpty()) return;
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/omarihamza/layouts/ModifyContactDialog.fxml"));
                            ModifyContactController contactController = new ModifyContactController();
                            contactController.setmContact(contacts.get(getIndex()));
                            loader.setController(contactController);
                            Utils.createDialog(loader, "Modify Contact", ez -> {
                                if (!contactController.isUpdateContact()) return;
                                Contact updatedContact = contactController.getUpdatedContact();
                                contacts.get(getIndex()).setName(updatedContact.getName());
                                contacts.get(getIndex()).setHouseNumber(updatedContact.getHouseNumber());
                                contacts.get(getIndex()).setEmail(updatedContact.getEmail());
                                contacts.get(getIndex()).setPhone(updatedContact.getPhone());
                                FileUtils.updateGroup(group);
                                groupMembersListView.setItems(null);
                                groupMembersListView.setItems(contacts);
                            });
                        });
                        contextMenu.getItems().add(modify);
                        contextMenu.getItems().add(delete);
                        content.setOnContextMenuRequested(event -> {
                            contextMenu.show(content.getParent(), event.getScreenX(), event.getScreenY());
                        });

                        if (item != null && !empty) { // <== test for null item and empty parameter
                            name.setText(item.getName());
                            apartmentNo.setText("House: " + item.getHouseNumber());
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
                            if (!listContainsContact(contacts, contact)) {
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

        excelImportButton.setOnAction(e -> {
            int counter = Utils.readContactsFromExcelFile(contacts, excelImportButton.getScene().getWindow());
            if (counter == -1) return;
            ArrayList<Contact> mContacts = new ArrayList<>(contacts);
            group.setContacts(mContacts);
            FileUtils.updateGroup(group);
            groupMembersListView.setItems(contacts);
            groupMembersTextView.setText(contacts.size() + " members");
            Utils.showPopup("Success", "Successfully added " + counter + " contacts", Alert.AlertType.INFORMATION);
        });

    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
