package omarihamza.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
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

public class CredentialsDialogController implements Initializable {


    @FXML
    private JFXTextField emailEditText;

    @FXML
    private JFXPasswordField passwordEditText;

    @FXML
    private JFXButton loginButton;


    private boolean login;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        AppSettings appSettings = FileUtils.loadSettings();
        if (appSettings != null && appSettings.isRememberUserEmail()) {
            emailEditText.setText(appSettings.getUserEmail());
        }

        loginButton.setOnMouseClicked(e -> {
            if (!emailEditText.getText().isEmpty() && !passwordEditText.getText().isEmpty()) {
                login = true;
                ((Stage) loginButton.getScene().getWindow()).close();
            } else {
                Utils.showPopup("Error", "Please enter your credentials", Alert.AlertType.ERROR);
            }
        });

    }

    public String getCredentials() {
        return emailEditText.getText() + " " + passwordEditText.getText();
    }

    public boolean isLogin() {
        return login;
    }
}
