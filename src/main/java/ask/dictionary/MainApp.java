package ask.dictionary;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by selimk on 3/10/2017.
 */
public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();

        Button randomButton = new Button("Get a random word");
        Button addButton = new Button("Add a new word");
        Button showAllButton = new Button("Show all words");
        HBox hBox = new HBox();
        hBox.setSpacing(50);
        hBox.setPadding(new Insets(15, 12, 15, 40));//(top/right/bottom/left)
        hBox.getChildren().addAll(addButton,randomButton,showAllButton);

        borderPane.setBottom(hBox);

        Scene scene = new Scene(borderPane,500,400);

        stage.setTitle("Dictionary App");
        stage.setScene(scene);
        stage.show();
    }
}
