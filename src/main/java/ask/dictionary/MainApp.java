package ask.dictionary;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


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
        randomButton.setOnAction(e ->
            initializeCheckBoxes(borderPane)
        );

        Button addButton = new Button("Add a new word");
        addButton.setOnAction(e ->
            addNewWord(borderPane)
        );

        Button showAllButton = new Button("Show all words");
        showAllButton.setOnAction(e ->
            initializeCheckBoxes(borderPane)
        );

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

    public void addNewWord(BorderPane borderPane){

        borderPane.setTop(null);

        VBox vbAddNewWord = new VBox();

        HBox hbWord = new HBox();
        Label wordLabel = new Label("Enter the new word: ");
        TextField wordTextField = new TextField();

        BooleanBinding wordTextFieldValid = Bindings.createBooleanBinding(() -> {
            if(wordTextField.getText().trim().length()==0) return false;
            return true;
        }, wordTextField.textProperty());

        hbWord.getChildren().addAll(wordLabel, wordTextField);


        HBox hbEngDesc = new HBox();
        Label engDescLabel = new Label("Enter word's description: ");
        TextField engDescTextField = new TextField();

        BooleanBinding engDescTextFieldValid = Bindings.createBooleanBinding(() -> {
            if(engDescTextField.getText().trim().length()==0) return false;
            return true;
        }, engDescTextField.textProperty());

        hbEngDesc.getChildren().addAll(engDescLabel, engDescTextField);


        HBox hbTurkishTranslate = new HBox();
        Label TurkishTranslateLabel = new Label("Enter word's Turkish translation: ");
        TextField TurkishTranslateTextField = new TextField();

        BooleanBinding TurkishTranslateTextFieldValid = Bindings.createBooleanBinding(() -> {
            if(TurkishTranslateTextField.getText().trim().length()==0) return false;
            return true;
        }, TurkishTranslateTextField.textProperty());

        hbTurkishTranslate.getChildren().addAll(TurkishTranslateLabel, TurkishTranslateTextField);


        HBox hbExampleSentence = new HBox();
        Label exampleSentenceLabel = new Label("Enter an example sentence for the word: ");
        TextField exampleSentenceTextField = new TextField();

        BooleanBinding exampleSentenceTextFieldValid = Bindings.createBooleanBinding(() -> {
            if(exampleSentenceTextField.getText().trim().length()==0) return false;
            return true;
        }, exampleSentenceTextField.textProperty());

        hbExampleSentence.getChildren().addAll(exampleSentenceLabel, exampleSentenceTextField);

        Button saveButton = new Button("Save");
        saveButton.disableProperty().bind(wordTextFieldValid.not().or(engDescTextFieldValid.not().or(TurkishTranslateTextFieldValid.not().or(exampleSentenceTextFieldValid.not()))));
        saveButton.setOnAction(e -> {
            Dictionary recordedObject = new Dictionary();
            recordedObject.setWord(wordTextField.getText());
            recordedObject.setWordsEnglishDescription(engDescTextField.getText());
            recordedObject.setWordsTurkishTranslation(TurkishTranslateTextField.getText());
            recordedObject.setExampleSentence(exampleSentenceTextField.getText());

            wordTextField.setText("");
            engDescTextField.setText("");
            TurkishTranslateTextField.setText("");
            exampleSentenceTextField.setText("");

            saveTheWord(recordedObject);
        });

        vbAddNewWord.getChildren().addAll(hbWord, hbEngDesc, hbTurkishTranslate, hbExampleSentence, saveButton);

        borderPane.setCenter(vbAddNewWord);
    }

    public void initializeCheckBoxes(BorderPane borderPane){

        borderPane.setCenter(null);

        HBox hBoxTop = new HBox();
        CheckBox wordCheckBox = new CheckBox("Word");
        wordCheckBox.setDisable(true);
        CheckBox wordsEnglishDescriptionCheckBox = new CheckBox("English Description");
        wordsEnglishDescriptionCheckBox.setDisable(true);
        CheckBox wordsTurkishTranslationCheckBox = new CheckBox("Turkish Translation");
        wordsTurkishTranslationCheckBox.setDisable(true);
        CheckBox exampleSentenceCheckBox = new CheckBox("Example Sentence");
        exampleSentenceCheckBox.setDisable(true);

        hBoxTop.getChildren().addAll(wordCheckBox, wordsEnglishDescriptionCheckBox, wordsTurkishTranslationCheckBox,
                exampleSentenceCheckBox);
        hBoxTop.setSpacing(80);
        hBoxTop.setPadding(new Insets(15, 12, 15, 70));

        borderPane.setTop(hBoxTop);
    }

    public void saveTheWord(Dictionary obj){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(obj);
        session.getTransaction().commit();
        session.close();

        sessionFactory.close();
    }

    public ObservableList<Dictionary> getListOfObjects(){

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        Session session = sessionFactory.openSession();

        ObservableList<Dictionary> list = FXCollections.observableArrayList(session.createQuery("from ask.dictionary.Dictionary").list());

        session.close(); sessionFactory.close();

        return list;
    }
}
