package omarihamza.cells;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import omarihamza.models.Message;


public class HistoryListCell extends ListCell<Message> {


    private VBox content;
    private StackPane hBox;
    private Text title, body, type, date;

    public HistoryListCell() {

        title = new Text();
        body = new Text();
        type = new Text();
        date = new Text();

        date.setStyle("-fx-fill: gray; -fx-font-size: 9");

        hBox = new StackPane();
        HBox typeBox = new HBox();
        typeBox.setAlignment(Pos.CENTER_LEFT);
//        typeBox.setPadding(new Insets(5));
        typeBox.getChildren().add(type);

        HBox dateBox = new HBox();
        dateBox.setAlignment(Pos.CENTER_RIGHT);
//        dateBox.setPadding(new Insets(5));
        dateBox.getChildren().add(date);


        hBox.getChildren().addAll(typeBox, dateBox);

        content = new VBox();
        content.getChildren().addAll(title, body, hBox);

        content.setBackground(new Background(new BackgroundFill(Paint.valueOf(Color.WHITE.toString()), new CornerRadii(10), Insets.EMPTY)));
        content.setPadding(new Insets(5));
        content.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(0.1))));
        content.setFillWidth(true);

    }

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            title.setText(item.getTitle());
            body.setText(item.getBody());
            date.setText("1-1-2018 20:00");
            type.setText("• Type: " + item.getType().toString() + " \t\t\t");
            switch (item.getType()) {
                case WhatsApp: {
                    type.setStyle("-fx-fill: forestgreen; -fx-font-style:italic ");
                    break;
                }

                case Viber: {
                    break;
                }

                case Email: {
                    type.setStyle("-fx-fill: firebrick; -fx-font-style: italic");
                    break;
                }

                case SMS: {
                    break;
                }
            }
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
