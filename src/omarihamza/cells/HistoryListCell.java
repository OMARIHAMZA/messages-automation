package omarihamza.cells;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import omarihamza.models.Message;


public class HistoryListCell extends ListCell<Message> {


    private VBox content;
    private StackPane hBox;
    private Text title, body, type, date;
    private boolean isRemoved = false;

    public HistoryListCell() {

        title = new Text();
        body = new Text();
        type = new Text();
        date = new Text();

        title.setStyle("-fx-fill: white;");
        body.setStyle("-fx-fill: white;");
        type.setStyle("-fx-fill: white;");
        date.setStyle("-fx-fill: white;");

        date.setStyle("-fx-fill: gray; -fx-font-size: 12; -fx-end-margin: 12");

        hBox = new StackPane();
        HBox typeBox = new HBox();
        typeBox.setAlignment(Pos.CENTER_LEFT);
//        typeBox.setPadding(new Insets(5));
        typeBox.getChildren().add(type);

        HBox dateBox = new HBox();
        dateBox.setAlignment(Pos.CENTER_RIGHT);
//        dateBox.setPadding(new Insets(5));
        dateBox.getChildren().add(date);


        hBox.getChildren().addAll(typeBox);

        VBox mVBox = new VBox(10);
        mVBox.getChildren().addAll(title, body, hBox);
        mVBox.setBackground(new Background(new BackgroundFill(Paint.valueOf("#419FD9"), new CornerRadii(10, 10, 10, 0, false), Insets.EMPTY)));
        mVBox.setPadding(new Insets(5));
        mVBox.setBorder(new Border(new BorderStroke(Paint.valueOf("#419FD9"), BorderStrokeStyle.SOLID, new CornerRadii(10, 10, 10, 0, false), new BorderWidths(0.1))));
        mVBox.setFillWidth(true);
        content = new VBox(5);
        content.getChildren().addAll(mVBox, dateBox);
        content.setFillWidth(true);

    }

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            title.setText(item.getTitle());
            body.setText(item.getBody());
            if (item.getTitle() == null && !isRemoved) {
                isRemoved = true;
                ((VBox) content.getChildren().get(0)).getChildren().remove(0);
            }
            date.setText(item.getDate());
            type.setText("• Type: " + item.getType().toString() + " \t\t\t");
            switch (item.getType()) {
                case WhatsApp: {
                    break;
                }

                case Viber: {
                    break;
                }

                case Email: {
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
