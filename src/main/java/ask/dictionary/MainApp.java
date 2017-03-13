package ask.dictionary;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

        HBox hBoxTop = new HBox();
        CheckBox word = new CheckBox("Word");
        word.setDisable(true);
        CheckBox wordsEnglishDescription = new CheckBox("English Description");
        wordsEnglishDescription.setDisable(true);
        CheckBox wordsTurkishTranslation = new CheckBox("Turkish Translation");
        wordsTurkishTranslation.setDisable(true);
        CheckBox exampleSentence = new CheckBox("Example Sentence");
        exampleSentence.setDisable(true);

        hBoxTop.getChildren().addAll(word,wordsEnglishDescription,wordsTurkishTranslation,exampleSentence);
        hBoxTop.setSpacing(80);
        hBoxTop.setPadding(new Insets(15, 12, 15, 70));

        borderPane.setTop(hBoxTop);

        Button randomButton = new Button("Get a random word");
        Button addButton = new Button("Add a new word");
        Button showAllButton = new Button("Show all words");
        HBox hBoxBottom = new HBox();
        hBoxBottom.setSpacing(125);
        hBoxBottom.setPadding(new Insets(15, 12, 15, 100));//(top/right/bottom/left)
        hBoxBottom.getChildren().addAll(addButton,randomButton,showAllButton);

        borderPane.setBottom(hBoxBottom);

        Scene scene = new Scene(borderPane,800,600);

        stage.setTitle("Dictionary App");
        stage.setScene(scene);
        stage.show();
    }
}
