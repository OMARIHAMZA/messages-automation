package omarihamza.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import omarihamza.models.Contact;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateContactDialogController implements Initializable {

    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXTextField phoneTextField;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXButton addContactButton;

    private boolean contactCreated = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        assignActions();

    }

    private void assignActions() {

        addContactButton.setOnAction(e -> {
            if (checkFields()) {
                contactCreated = true;
                ((Stage) addContactButton.getScene().getWindow()).close();
            }
        });

        nameTextField.setOnMouseClicked(e -> nameTextField.setUnFocusColor(Paint.valueOf("#14a4e4")));

        phoneTextField.setOnMouseClicked(e -> phoneTextField.setUnFocusColor(Paint.valueOf("#14a4e4")));

    }

    private boolean checkFields() {

        boolean fieldsFilled = true;

        if (nameTextField.getText().isEmpty()) {
            nameTextField.setUnFocusColor(Color.RED);
            fieldsFilled = false;
        }

        if (phoneTextField.getText().isEmpty()) {
            phoneTextField.setUnFocusColor(Color.RED);
            fieldsFilled = false;
        }

        return fieldsFilled;
    }

    public Contact getContact() {
        if (contactCreated)
            return new Contact(nameTextField.getText(), phoneTextField.getText(), emailTextField.getText());
        else
            return null;
    }
}
