package omarihamza.controllers;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import omarihamza.callbacks.DeleteGroupCallback;
import omarihamza.callbacks.RefreshHistoryCallback;
import omarihamza.cells.GroupListCell;
import omarihamza.cells.HistoryListCell;
import omarihamza.dialogs.CredentialsDialogController;
import omarihamza.dialogs.GroupInfoDialogController;
import omarihamza.dialogs.MessageDialogController;
import omarihamza.dialogs.SettingsController;
import omarihamza.models.*;
import omarihamza.utils.EmailAPI;
import omarihamza.utils.FileUtils;
import omarihamza.utils.Utils;
import omarihamza.utils.WhatsAppAPI;

import javax.mail.internet.AddressException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static omarihamza.utils.FileUtils.FILE_NAME;

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

    @FXML
    private HBox dotsImageHbox;

    private ObservableList<Group> data = FXCollections.observableArrayList();

    @SuppressWarnings("ALL")
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        data.addAll(FileUtils.loadGroups());
        contactsListView.setItems(data);

        contactsListView.setCellFactory(listView -> new GroupListCell(new DeleteGroupCallback() {
            @Override
            public void deleteGroup(int index) {
                if (index != -1) {
                    Optional<ButtonType> result = Utils.showConfirmationDialog("Warning", "Are you sure you want to delete \"" + data.get(index).getTitle() + "\" Group? ");
                    if (result.get() == ButtonType.OK) {
                        data.remove(index);
                        FileUtils.storeGroups(new ArrayList<>(data), new File(FILE_NAME));
                        contactsListView.setItems(null);
                        contactsListView.setItems(data);
                    }
                }
            }
        }));
        contactsListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                groupTitleText.setText("");
                dotsImageHbox.setVisible(false);
                sendMessageButton.setVisible(false);
                historyListView.setItems(null);
            }
        });
        contactsListView.setOnMouseClicked(event -> {
            if (contactsListView.getSelectionModel().getSelectedIndex() == -1) {
                return;
            }
            groupTitleText.setText(data.get(contactsListView.getSelectionModel().getSelectedIndex()).getTitle());
            ObservableList<Message> messages = FXCollections.observableArrayList();
            messages.addAll(data.get(contactsListView.getSelectionModel().getSelectedIndex()).getMessages());
            Collections.reverse(messages);
            historyListView.setItems(messages);
            historyListView.setCellFactory(listView -> new HistoryListCell());
            dotsImageHbox.setVisible(true);
            sendMessageButton.setVisible(true);
        });

        exitText.setOnMouseClicked(e -> Platform.exit());
        assignActions();
        sendMessageButton.setVisible(false);
        dotsImageHbox.setVisible(false);
        historyListView.setVisible(true);
        historyListView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
        });
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
                            if (contactsListView.getSelectionModel().getSelectedIndex() == -1) return;
                            data.get(contactsListView.getSelectionModel().getSelectedIndex()).getMessages().add(message);
                            FileUtils.updateGroup(data.get(contactsListView.getSelectionModel().getSelectedIndex()));
                            updateHistory();
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

        settingsBox.setOnMouseClicked(e -> {
            Parent root = null;
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/omarihamza/layouts/SettingsDialog.fxml"));

            Stage stage = new Stage();
            omarihamza.dialogs.SettingsController settingsController = new SettingsController();
            loader.setController(settingsController);
            try {
                root = loader.load();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //noinspection all
            Scene scene = new Scene(root);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        });


    }

    private void sendEmail(Message message) {

        Stage mStage = new Stage();
        StringBuilder email = new StringBuilder();
        StringBuilder password = new StringBuilder();

        Parent root = null;
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("/omarihamza/layouts/CredentialsDialog.fxml"));

        Stage stage = new Stage();
        if (contactsListView.getSelectionModel().getSelectedIndex() == -1) return;

        AppSettings appSettings = FileUtils.loadSettings();
        if (appSettings == null || appSettings.getHost() == null || appSettings.getPort() == null) {
            Utils.showPopup("Error", "Please set the host and port from Settings.", Alert.AlertType.ERROR);
            return;
        }

        stage.setTitle(data.get(contactsListView.getSelectionModel().getSelectedIndex()).getTitle());
        CredentialsDialogController controller = new CredentialsDialogController();
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
            if (controller.isLogin()) {
                Utils.showPopup("Sending Email...", "This Process will take some time,\nPress OK and wait for your email to be sent!", Alert.AlertType.INFORMATION);
            } else {
                return;
            }

            email.append(controller.getCredentials().split(" ")[0]);
            password.append(controller.getCredentials().split(" ")[1]);

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
                EmailAPI.sendEmail(data.get(contactsListView.getSelectionModel().getSelectedIndex()), email.toString(), password.toString(), message.getTitle(), message.getBody(), recipients, this::updateHistory, mStage);
            } catch (AddressException e) {
                e.printStackTrace();
            }
        }));

        stage.showAndWait();


    }

    private void sendWhatsAppMessage(Message message) {

        HashMap<String, String> hashMap = new HashMap<>();

        for (Contact contact : data.get(contactsListView.getSelectionModel().getSelectedIndex()).getContacts()) {
            hashMap.put(contact.getPhone(), message.getBody());
        }

        Platform.runLater(() -> new Thread(() -> WhatsAppAPI.getInstance().sendMessages(data.get(contactsListView.getSelectionModel().getSelectedIndex()), hashMap, this::updateHistory)).start());
    }

    @SuppressWarnings("ALL")
    private void updateHistory() {
        if (contactsListView.getSelectionModel().getSelectedIndex() == -1) return;
        historyListView.setItems(null);
        ObservableList<Message> messages = FXCollections.observableArrayList();
        messages.addAll(data.get(contactsListView.getSelectionModel().getSelectedIndex()).getMessages());
        Collections.reverse(messages);
        historyListView.setItems(messages);
    }
}
