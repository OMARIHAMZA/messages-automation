package omarihamza.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import omarihamza.models.Contact;
import omarihamza.utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyContactController implements Initializable {

    @FXML
    private JFXTextField apartmentNumberTextField;

    @FXML
    private JFXTextField phoneTextField;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXButton saveChangesButton;

    @FXML
    private JFXTextField nameTextField;

    private Contact mContact;

    private boolean updateContact = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        showData();
        assignActions();
    }

    private void assignActions() {
        saveChangesButton.setOnAction(e -> {
            try {
                if (nameTextField.getText().isEmpty() || phoneTextField.getText().isEmpty() || apartmentNumberTextField.getText().isEmpty()){
                    Utils.showPopup("Error", "Please Fill Required Fields and try again.", Alert.AlertType.ERROR);
                    return;
                }
            }catch (Exception ez){
                Utils.showPopup("Error", "Please Fill Required Fields and try again.", Alert.AlertType.ERROR);
                return;
            }
            updateContact = true;
            ((Stage) saveChangesButton.getScene().getWindow()).close();
        });
    }

    private void showData() {
        nameTextField.setText(mContact.getName());
        phoneTextField.setText(mContact.getPhone());
        apartmentNumberTextField.setText(mContact.getApartmentNumber());
        emailTextField.setText(mContact.getEmail());
    }

    Contact getUpdatedContact() {
        return new Contact(nameTextField.getText(), phoneTextField.getText(), emailTextField.getText(), apartmentNumberTextField.getText());
    }

    void setmContact(Contact mContact) {
        this.mContact = mContact;
    }

    public boolean isUpdateContact() {
        return updateContact;
    }
}
