package omarihamza.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import omarihamza.models.AppSettings;
import omarihamza.utils.FileUtils;
import omarihamza.utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXCheckBox rememberEmailCheckBox;

    @FXML
    private JFXTextField hostTextField;

    @FXML
    private JFXTextField portTextField;

    @FXML
    private JFXButton saveEmailSettingsButton;

    @FXML
    private JFXTextField whatsAppTimeoutTextField;

    @FXML
    private JFXButton saveWhatsAppSettingsButton;


    @FXML
    private JFXTextField nameColumnTextField;

    @FXML
    private JFXTextField apartmentColumnTextField;

    @FXML
    private JFXTextField phoneColumnTextField;

    @FXML
    private JFXTextField emailColumnTextField;

    @FXML
    private JFXButton saveExcelSettingsButton;

    private AppSettings appSettings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        appSettings = FileUtils.loadSettings();

        displaySettings();

        assignActions();

    }

    private void displaySettings() {
        if (appSettings != null) {
            emailTextField.setText(appSettings.getUserEmail());
            rememberEmailCheckBox.selectedProperty().set(appSettings.isRememberUserEmail());
            portTextField.setText(appSettings.getPort());
            hostTextField.setText(appSettings.getHost());
            whatsAppTimeoutTextField.setText(appSettings.getWhatsAppTimeout() + "");
            nameColumnTextField.setText(appSettings.getNameColumn());
            phoneColumnTextField.setText(appSettings.getPhoneColumn());
            emailColumnTextField.setText(appSettings.getEmailColumn());
            apartmentColumnTextField.setText(appSettings.getApartmentColumn());
        }
    }

    private void assignActions() {
        saveEmailSettingsButton.setOnAction(e -> {
            appSettings = FileUtils.loadSettings();
            if (!hostTextField.getText().isEmpty() && !portTextField.getText().isEmpty()) {
                if (appSettings == null) {
                    appSettings = new AppSettings(emailTextField.getText(),
                            rememberEmailCheckBox.isSelected(),
                            hostTextField.getText(),
                            portTextField.getText(), 60);
                } else {
                    appSettings.setUserEmail(emailTextField.getText());
                    appSettings.setRememberUserEmail(rememberEmailCheckBox.isSelected());
                    appSettings.setHost(hostTextField.getText());
                    appSettings.setPort(portTextField.getText());
                }
                FileUtils.storeSettings(appSettings);
                ((Stage) saveEmailSettingsButton.getScene().getWindow()).close();
            } else {
                Utils.showPopup("Error", "Please enter the host and port.", Alert.AlertType.ERROR);
            }
        });

        saveWhatsAppSettingsButton.setOnAction(e -> {
            if (appSettings == null)
                appSettings = new AppSettings();

            if (whatsAppTimeoutTextField.getText().isEmpty()) {
                appSettings.setWhatsAppTimeout(60);
            } else {
                appSettings.setWhatsAppTimeout(Integer.parseInt(whatsAppTimeoutTextField.getText()));
            }
            FileUtils.storeSettings(appSettings);
            ((Stage) saveEmailSettingsButton.getScene().getWindow()).close();
        });

        saveExcelSettingsButton.setOnAction(e -> {

            if (appSettings == null) {
                appSettings = new AppSettings();
            }

            if (nameColumnTextField.getText().isEmpty()
                    || apartmentColumnTextField.getText().isEmpty()
                    || phoneColumnTextField.getText().isEmpty()
                    || emailColumnTextField.getText().isEmpty()) {
                Utils.showPopup("Error", "Please specify all columns and try again", Alert.AlertType.ERROR);
                return;
            }

            appSettings.setNameColumn(nameColumnTextField.getText());
            appSettings.setEmailColumn(emailColumnTextField.getText());
            appSettings.setApartmentColumn(apartmentColumnTextField.getText());
            appSettings.setPhoneColumn(phoneColumnTextField.getText());

            FileUtils.storeSettings(appSettings);

            ((Stage) saveExcelSettingsButton.getScene().getWindow()).close();
        });
    }
}
