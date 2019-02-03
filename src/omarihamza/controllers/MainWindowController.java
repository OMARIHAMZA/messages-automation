package omarihamza.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import omarihamza.cells.GroupListCell;
import omarihamza.dialogs.GroupInfoDialogController;
import omarihamza.models.Group;
import omarihamza.utils.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private JFXTextField groupNameTextField;

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
    private HBox contactsBox, newGroupBox, importBox, settingsBox, logoutBox, groupTitleHBox;

    private ObservableList<Group> data = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        data.addAll(FileUtils.loadGroups());
        contactsListView.setItems(data);

        contactsListView.setCellFactory(listView -> new GroupListCell());
        contactsListView.setOnMouseClicked(event -> {
            if (contactsListView.getSelectionModel().getSelectedIndex() == -1) return;
            groupTitleText.setText(data.get(contactsListView.getSelectionModel().getSelectedIndex()).getTitle());
        });

        exitText.setOnMouseClicked(e -> Platform.exit());

        assignActions();
    }

    private void assignActions() {
        newGroupBox.setOnMouseClicked(e -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/omarihamza/layouts/CreateGroupDialog.fxml"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Create Group");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setOnHiding(ee -> Platform.runLater(() -> {
                data.setAll(FileUtils.loadGroups());
                contactsListView.setItems(data);
            }));
            stage.showAndWait();
            /*Group group = new Group("Test-" + new Random().nextInt(100), null);
            data.setAll(FileUtils.storeGroup(group));
            contactsListView.setItems(data);*/

        });

        groupTitleHBox.setOnMouseClicked(e -> {

            Parent root = null;
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("/omarihamza/layouts/GroupInfoDialog.fxml"));

            Stage stage = new Stage();
            stage.setTitle(data.get(contactsListView.getSelectionModel().getSelectedIndex()).getTitle());
            GroupInfoDialogController controller = new GroupInfoDialogController(data.get(contactsListView.getSelectionModel().getSelectedIndex()));
            loader.setController(controller);
            try {
                root = loader.load();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Scene scene = new Scene(root);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            /*stage.setOnHiding(ee -> Platform.runLater(() -> {
                data.setAll(FileUtils.loadGroups());
                contactsListView.setItems(data);
            }));*/
            stage.show();

        });

    }
}
