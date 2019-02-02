package omarihamza.controllers;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import omarihamza.cells.ContactsListCell;
import omarihamza.models.Contact;
import omarihamza.models.Group;
import omarihamza.utils.FileUtils;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private AnchorPane rootView;

    @FXML
    private ListView contactsListView;

    @FXML
    private TextField searchTextField;

    @FXML
    private Text exitText;

    @FXML
    private Text groupTitleText;

    @FXML
    private HBox contactsBox, newGroupBox, importBox, settingsBox, logoutBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<Group> data = FXCollections.observableArrayList();
        data.addAll(FileUtils.loadGroups());
        contactsListView.setItems(data);

        contactsListView.setCellFactory(listView -> new ContactsListCell());
        contactsListView.setOnMouseClicked(event -> {
            if (contactsListView.getSelectionModel().getSelectedIndex() == -1) return;
            groupTitleText.setText(data.get(contactsListView.getSelectionModel().getSelectedIndex()).getTitle());
        });

        exitText.setOnMouseClicked(e -> Platform.exit());

        newGroupBox.setOnMouseClicked(e -> {

            Group group = new Group("Test-" + new Random().nextInt(100), null);
            data.setAll(FileUtils.storeGroup(group));
            contactsListView.setItems(data);

        });


    }
}
