package omarihamza.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import omarihamza.models.Message;
import omarihamza.models.MessageType;
import omarihamza.utils.Utils;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MessageDialogController implements Initializable {


    @FXML
    private JFXRadioButton whatsAppRadioButton;

    @FXML
    private JFXRadioButton viberRadioButton;

    @FXML
    private JFXRadioButton emailRadioButton;

    @FXML
    private JFXRadioButton smsRadioButton;

    @FXML
    private JFXTextField titleTextField;

    @FXML
    private JFXTextArea bodyTextArea;

    @FXML
    private JFXButton sendMessageButton;

    private ToggleGroup toggleGroup;

    private Message message;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        assignActions();

    }

    private void assignActions() {

        //RadioButtons
        toggleGroup = new ToggleGroup();
        whatsAppRadioButton.setToggleGroup(toggleGroup);
        viberRadioButton.setToggleGroup(toggleGroup);
        smsRadioButton.setToggleGroup(toggleGroup);
        emailRadioButton.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (((RadioButton) toggleGroup.getSelectedToggle()).getText().equalsIgnoreCase("email")) {
                titleTextField.setDisable(false);
            } else {
                titleTextField.setDisable(true);
            }
        });

        //
        sendMessageButton.setOnAction(e -> {
            if (checkFields()) {
                Optional<ButtonType> result = Utils.showConfirmationDialog("Confirmation",
                        ((RadioButton) toggleGroup.getSelectedToggle()).getText() + " Message will be sent to the group.");
                if (result.get() == ButtonType.OK) {
                    switch (((RadioButton) toggleGroup.getSelectedToggle()).getText().toLowerCase()) {
                        case "viber": {
                            message = new Message(null, bodyTextArea.getText(), MessageType.Viber);
                            break;
                        }

                        case "whatsapp": {
                            message = new Message(null, bodyTextArea.getText(), MessageType.WhatsApp);
                            break;
                        }

                        case "sms": {
                            message = new Message(null, bodyTextArea.getText(), MessageType.SMS);
                            break;
                        }

                        case "email": {
                            message = new Message(titleTextField.getText(), bodyTextArea.getText(), MessageType.Email);
                            break;
                        }

                    }

                    ((Stage) sendMessageButton.getScene().getWindow()).close();

                }
            }
        });

        titleTextField.setOnMouseClicked(e -> titleTextField.setUnFocusColor(Paint.valueOf("#276899")));

        bodyTextArea.setOnMouseClicked(e -> bodyTextArea.setUnFocusColor(Paint.valueOf("#276899")));


    }

    private boolean checkFields() {
        if (toggleGroup.getSelectedToggle() == null) {
            Utils.showPopup("Error", "Select message type", Alert.AlertType.ERROR);
            return false;
        }
        if (((RadioButton) toggleGroup.getSelectedToggle()).getText().equalsIgnoreCase("email")) {
            if (titleTextField.getText().isEmpty()) {
                titleTextField.setUnFocusColor(Color.RED);
                return false;
            }
        }
        if (bodyTextArea.getText().isEmpty()) {
            bodyTextArea.setUnFocusColor(Color.RED);
            return false;
        }
        return true;
    }

    public Message getMessage() {
        return message;
    }
}
