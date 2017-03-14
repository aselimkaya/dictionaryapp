package ask.dictionary;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    static ObservableList<Dictionary> list;

    TableColumn<Dictionary, String> wordColumn;
    TableColumn<Dictionary, String> engDescColumn;
    TableColumn<Dictionary, String> TurkishTranslateColumn;
    TableColumn<Dictionary, String> exampleSentenceColumn;

    public static void main(String[] args) {

        fillList();

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
        showAllButton.setOnAction(e -> {
            initializeCheckBoxes(borderPane);
            fillTheTable(borderPane);
        });

        HBox hBoxBottom = new HBox();
        hBoxBottom.setSpacing(125);
        hBoxBottom.setPadding(new Insets(15, 12, 70, 100));//(top/right/bottom/left)
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

        BooleanBinding wordTextFieldValid = Bindings.createBooleanBinding(() ->
                wordTextField.getText().trim().length() != 0, wordTextField.textProperty());

        hbWord.getChildren().addAll(wordLabel, wordTextField);


        HBox hbEngDesc = new HBox();
        Label engDescLabel = new Label("Enter word's description: ");
        TextField engDescTextField = new TextField();

        BooleanBinding engDescTextFieldValid = Bindings.createBooleanBinding(() ->
                engDescTextField.getText().trim().length() != 0, engDescTextField.textProperty());

        hbEngDesc.getChildren().addAll(engDescLabel, engDescTextField);


        HBox hbTurkishTranslate = new HBox();
        Label TurkishTranslateLabel = new Label("Enter word's Turkish translation: ");
        TextField TurkishTranslateTextField = new TextField();

        BooleanBinding TurkishTranslateTextFieldValid = Bindings.createBooleanBinding(() ->
                TurkishTranslateTextField.getText().trim().length() != 0, TurkishTranslateTextField.textProperty());

        hbTurkishTranslate.getChildren().addAll(TurkishTranslateLabel, TurkishTranslateTextField);


        HBox hbExampleSentence = new HBox();
        Label exampleSentenceLabel = new Label("Enter an example sentence for the word: ");
        TextField exampleSentenceTextField = new TextField();

        BooleanBinding exampleSentenceTextFieldValid = Bindings.createBooleanBinding(() ->
                exampleSentenceTextField.getText().trim().length() != 0, exampleSentenceTextField.textProperty());

        hbExampleSentence.getChildren().addAll(exampleSentenceLabel, exampleSentenceTextField);

        Button saveButton = new Button("Save");

        saveButton.disableProperty().bind(wordTextFieldValid.not()
                .or(engDescTextFieldValid.not()
                    .or(TurkishTranslateTextFieldValid.not()
                        .or(exampleSentenceTextFieldValid.not()))));

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
        wordCheckBox.setSelected(true);

        wordCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!wordCheckBox.isSelected())
                    wordColumn.setVisible(false);
                else
                    wordColumn.setVisible(true);
            }
        });


        CheckBox wordsEnglishDescriptionCheckBox = new CheckBox("English Description");
        wordsEnglishDescriptionCheckBox.setSelected(true);

        wordsEnglishDescriptionCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!wordsEnglishDescriptionCheckBox.isSelected())
                    engDescColumn.setVisible(false);
                else
                    engDescColumn.setVisible(true);
            }
        });


        CheckBox wordsTurkishTranslationCheckBox = new CheckBox("Turkish Translation");
        wordsTurkishTranslationCheckBox.setSelected(true);

        wordsTurkishTranslationCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!wordsTurkishTranslationCheckBox.isSelected())
                    TurkishTranslateColumn.setVisible(false);
                else
                    TurkishTranslateColumn.setVisible(true);
            }
        });


        CheckBox exampleSentenceCheckBox = new CheckBox("Example Sentence");
        exampleSentenceCheckBox.setSelected(true);

        exampleSentenceCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!exampleSentenceCheckBox.isSelected())
                    exampleSentenceColumn.setVisible(false);
                else
                    exampleSentenceColumn.setVisible(true);
            }
        });


        hBoxTop.getChildren().addAll(wordCheckBox, wordsEnglishDescriptionCheckBox, wordsTurkishTranslationCheckBox,
                exampleSentenceCheckBox);
        hBoxTop.setSpacing(80);
        hBoxTop.setPadding(new Insets(15, 12, 15, 70));

        borderPane.setTop(hBoxTop);
    }

    public void fillTheTable(BorderPane borderPane){

        borderPane.setCenter(null);

        wordColumn = new TableColumn<>("Word");
        wordColumn.setMinWidth(100);
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));

        engDescColumn = new TableColumn<>("Word's English Description");
        engDescColumn.setMinWidth(300);
        engDescColumn.setCellValueFactory(new PropertyValueFactory<>("wordsEnglishDescription"));

        TurkishTranslateColumn = new TableColumn<>("Word's Turkish Translation");
        TurkishTranslateColumn.setMinWidth(300);
        TurkishTranslateColumn.setCellValueFactory(new PropertyValueFactory<>("wordsTurkishTranslation"));

        exampleSentenceColumn = new TableColumn<>("Example Sentence");
        exampleSentenceColumn.setMinWidth(300);
        exampleSentenceColumn.setCellValueFactory(new PropertyValueFactory<>("exampleSentence"));

        TableView<Dictionary> table = new TableView<>();
        table.setItems(getListOfObjects());
        table.getColumns().addAll(wordColumn, engDescColumn, TurkishTranslateColumn, exampleSentenceColumn);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(table);

        borderPane.setCenter(vbox);
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
        return list;
    }

    public static void fillList(){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        Session session = sessionFactory.openSession();

        list = FXCollections.observableArrayList(session.createQuery("from ask.dictionary.Dictionary").list());

        session.close(); sessionFactory.close();
    }
}