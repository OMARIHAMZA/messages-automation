package omarihamza;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import omarihamza.utils.WhatsAppAPI;

public class Main extends Application {

    private double xOffset = 0, yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layouts/MainWindow.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/omarihamza/resources/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        initWindowDrag(root, primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void initWindowDrag(Parent root, Stage primaryStage) {
        root.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });
    }
}
