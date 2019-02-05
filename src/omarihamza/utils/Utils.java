package omarihamza.utils;

import com.sun.istack.internal.Nullable;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import omarihamza.dialogs.GroupsImportDialogController;
import omarihamza.models.Contact;
import omarihamza.models.Group;

import java.io.IOException;
import java.util.ArrayList;

public class Utils {

    public static Scene createDialog(FXMLLoader loader, String dialogTitle, @Nullable EventHandler onHidingHandler) {
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle(dialogTitle);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setOnHiding(onHidingHandler);
        stage.showAndWait();
        return scene;
    }

}
