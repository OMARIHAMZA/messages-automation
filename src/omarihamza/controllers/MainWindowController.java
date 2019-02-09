package omarihamza.controllers;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import omarihamza.cells.GroupListCell;
import omarihamza.cells.HistoryListCell;
import omarihamza.dialogs.GroupInfoDialogController;
import omarihamza.dialogs.MessageDialogController;
import omarihamza.models.Contact;
import omarihamza.models.Group;
import omarihamza.models.Message;
import omarihamza.models.MessageType;
import omarihamza.utils.EmailAPI;
import omarihamza.utils.FileUtils;
import omarihamza.utils.Utils;
import omarihamza.utils.WhatsAppAPI;

import javax.mail.internet.AddressException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainWindowController implements Initializable {

    @FXML
    private JFXTextField groupNameTextField;

    @FXML
    private AnchorPane rootView;

    @FXML
    private ListView contactsListView;

    @FXML
    private JFXListView historyListView;

    @FXML
    private TextField searchTextField;

    @FXML
    private Text exitText;

    @FXML
    private Text groupTitleText;

    @FXML
    private HBox contactsBox, newGroupBox, importBox, settingsBox, logoutBox, groupTitleHBox;

    @FXML
    private Button sendMessageButton;

    private ObservableList<Group> data = FXCollections.observableArrayList();

    @SuppressWarnings("ALL")
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        data.addAll(FileUtils.loadGroups());
        contactsListView.setItems(data);

        contactsListView.setCellFactory(listView -> new GroupListCell());
        contactsListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                groupTitleText.setText("");
                sendMessageButton.setVisible(false);
            }
        });
        contactsListView.setOnMouseClicked(event -> {
            if (contactsListView.getSelectionModel().getSelectedIndex() == -1) {
                return;
            }
            groupTitleText.setText(data.get(contactsListView.getSelectionModel().getSelectedIndex()).getTitle());
            sendMessageButton.setVisible(true);
        });

        exitText.setOnMouseClicked(e -> Platform.exit());

        assignActions();

        sendMessageButton.setVisible(false);

        ObservableList<Message> messages = FXCollections.observableArrayList();

        messages.add(new Message("Title", "body", MessageType.Email));
        messages.add(new Message("Title", "body", MessageType.Email));
        messages.add(new Message("Title", "body", MessageType.SMS));
        messages.add(new Message("Title", "body", MessageType.WhatsApp));

        historyListView.setItems(messages);
        historyListView.setCellFactory(listView -> new HistoryListCell());

    }

    private void assignActions() {
        newGroupBox.setOnMouseClicked(e -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/omarihamza/layouts/CreateGroupDialog.fxml"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //noinspection all
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Create Group");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setOnHiding(ee -> Platform.runLater(() -> {
                data.setAll(FileUtils.loadGroups());
                //noinspection all
                contactsListView.setItems(data);
            }));
            stage.showAndWait();
        });

        groupTitleHBox.setOnMouseClicked(e -> {

            Parent root = null;
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/omarihamza/layouts/GroupInfoDialog.fxml"));

            Stage stage = new Stage();
            if (contactsListView.getSelectionModel().getSelectedIndex() == -1) return;
            stage.setTitle(data.get(contactsListView.getSelectionModel().getSelectedIndex()).getTitle());
            GroupInfoDialogController controller = new GroupInfoDialogController(data.get(contactsListView.getSelectionModel().getSelectedIndex()));
            loader.setController(controller);
            try {
                root = loader.load();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //noinspection all
            Scene scene = new Scene(root);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setOnHiding(ee -> Platform.runLater(() -> {
                data.setAll(FileUtils.loadGroups());
                //noinspection all
                contactsListView.setItems(data);
            }));
            stage.show();
        });

        sendMessageButton.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/omarihamza/layouts/MessageDialog.fxml"));
            Utils.createDialog(loader, "Send Message", ee -> {

                MessageDialogController controller = loader.getController();
                Message message = controller.getMessage();
                if (message != null) {

                    switch (message.getType()) {

                        case SMS: {

                            break;
                        }

                        case Email: {
                            sendEmail(message);
                            break;
                        }

                        case Viber: {

                            break;
                        }

                        case WhatsApp: {
                            sendWhatsAppMessage(message);
                            break;
                        }
                    }


                }

            });
        });


    }

    private void sendEmail(Message message) {

        ArrayList<String> recipients = new ArrayList<>();
        Pattern p = Pattern.compile(".+@.+\\..+");

        for (Contact contact : data.get(contactsListView.getSelectionModel().getSelectedIndex()).getContacts()) {
            Matcher m = p.matcher(contact.getEmail());
            if (m.matches())
                recipients.add(contact.getEmail());
        }

        if (recipients.isEmpty()) {
            Utils.showPopup("Error", "There are no recipients", Alert.AlertType.ERROR);
            return;
        }

        try {
            EmailAPI.sendEmail("omarihamza@outlook.com", "Hamzahamza97", message.getTitle(), message.getBody(), recipients);
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }

    private void sendWhatsAppMessage(Message message) {

        HashMap<String, String> hashMap = new HashMap<>();

        for (Contact contact : data.get(contactsListView.getSelectionModel().getSelectedIndex()).getContacts()) {
            hashMap.put(contact.getPhone(), message.getBody());
        }

        Platform.runLater(() -> new Thread(() -> WhatsAppAPI.getInstance().sendMessages(hashMap)).start());
    }
}
