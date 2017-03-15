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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Random;


/**
 * Created by selimk on 3/10/2017.
 */
public class MainApp extends Application {

    static ObservableList<Dictionary> list;

    TableView<Dictionary> table;

    TableColumn<Dictionary, String> wordColumn;
    TableColumn<Dictionary, String> engDescColumn;
    TableColumn<Dictionary, String> TurkishTranslateColumn;
    TableColumn<Dictionary, String> exampleSentenceColumn;

    static boolean wordCheckBoxFlag;
    static boolean wordsEnglishDescriptionCheckBoxFlag;
    static boolean wordsTurkishTranslationCheckBoxFlag;
    static boolean exampleSentenceCheckBoxFlag;

    public static void main(String[] args) {

        fillList();

        wordCheckBoxFlag = true;
        wordsEnglishDescriptionCheckBoxFlag = true;
        wordsTurkishTranslationCheckBoxFlag = true;
        exampleSentenceCheckBoxFlag = true;

        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();

        Button randomButton = new Button("Get a random word");
        randomButton.setOnAction(e -> {
            initializeCheckBoxes(borderPane);
            fillTheTable(borderPane, Option.RANDOM);
        });

        Button addButton = new Button("Add a new word");
        addButton.setOnAction(e ->
            addNewWord(borderPane)
        );

        Button showAllButton = new Button("Show all words");
        showAllButton.setOnAction(e -> {
            initializeCheckBoxes(borderPane);
            fillTheTable(borderPane, Option.ALL);
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteAnObject());

        HBox hBoxBottom = new HBox();
        hBoxBottom.setSpacing(125);
        hBoxBottom.setPadding(new Insets(15, 12, 70, 100));//(top/right/bottom/left)
        hBoxBottom.getChildren().addAll(addButton,randomButton,showAllButton, deleteButton);

        borderPane.setBottom(hBoxBottom);

        Scene scene = new Scene(borderPane,800,600);

        stage.setTitle("Dictionary App");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteAnObject(){
        ObservableList<Dictionary> selectedWords, allWords;
        allWords = table.getItems();
        selectedWords = table.getSelectionModel().getSelectedItems();

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        for(Dictionary dict: selectedWords){
            session.delete(dict);
            allWords.remove(dict);
        }
        session.getTransaction().commit();
        session.close();

        sessionFactory.close();
    }

    public void addNewWord(BorderPane borderPane){

        borderPane.setTop(null);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(120);
        gridPane.setVgap(25);
        gridPane.setPadding(new Insets(130, 120, 0, 10));

        Label wordLabel = new Label("Enter the new word:");
        gridPane.add(wordLabel, 1, 0);
        TextField wordTextField = new TextField();
        gridPane.add(wordTextField, 2, 0);

        BooleanBinding wordTextFieldValid = Bindings.createBooleanBinding(() ->
                wordTextField.getText().trim().length() != 0, wordTextField.textProperty());


        Label engDescLabel = new Label("Enter word's description:");
        gridPane.add(engDescLabel, 1, 1);
        TextField engDescTextField = new TextField();
        gridPane.add(engDescTextField, 2, 1);

        BooleanBinding engDescTextFieldValid = Bindings.createBooleanBinding(() ->
                engDescTextField.getText().trim().length() != 0, engDescTextField.textProperty());



        Label TurkishTranslateLabel = new Label("Enter word's Turkish translation:");
        gridPane.add(TurkishTranslateLabel, 1, 2);
        TextField TurkishTranslateTextField = new TextField();
        gridPane.add(TurkishTranslateTextField, 2, 2);

        BooleanBinding TurkishTranslateTextFieldValid = Bindings.createBooleanBinding(() ->
                TurkishTranslateTextField.getText().trim().length() != 0, TurkishTranslateTextField.textProperty());



        Label exampleSentenceLabel = new Label("Enter an example sentence for the word:");
        gridPane.add(exampleSentenceLabel, 1, 3);
        TextField exampleSentenceTextField = new TextField();
        gridPane.add(exampleSentenceTextField, 2, 3);

        BooleanBinding exampleSentenceTextFieldValid = Bindings.createBooleanBinding(() ->
                exampleSentenceTextField.getText().trim().length() != 0, exampleSentenceTextField.textProperty());



        Button saveButton = new Button("Save");

        saveButton.disableProperty().bind(wordTextFieldValid.not().or(TurkishTranslateTextFieldValid.not()));

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
        gridPane.add(saveButton, 1, 4);

        borderPane.setCenter(gridPane);
    }

    public void initializeCheckBoxes(BorderPane borderPane){

        borderPane.setCenter(null);

        HBox hBoxTop = new HBox();

        CheckBox wordCheckBox = new CheckBox("Word");
        wordCheckBox.setSelected(wordCheckBoxFlag);

        wordCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!wordCheckBox.isSelected()) {
                    wordColumn.setVisible(false);
                    wordCheckBoxFlag = false;
                }
                else{
                    wordCheckBoxFlag = true;
                    wordColumn.setVisible(true);
                }
            }
        });


        CheckBox wordsEnglishDescriptionCheckBox = new CheckBox("English Description");
        wordsEnglishDescriptionCheckBox.setSelected(wordsEnglishDescriptionCheckBoxFlag);

        wordsEnglishDescriptionCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(wordsEnglishDescriptionCheckBox.isSelected()) {
                    engDescColumn.setVisible(true);
                    wordsEnglishDescriptionCheckBoxFlag = true;
                }
                else{
                    engDescColumn.setVisible(false);
                    wordsEnglishDescriptionCheckBoxFlag = false;
                }
            }
        });


        CheckBox wordsTurkishTranslationCheckBox = new CheckBox("Turkish Translation");
        wordsTurkishTranslationCheckBox.setSelected(wordsTurkishTranslationCheckBoxFlag);

        wordsTurkishTranslationCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!wordsTurkishTranslationCheckBox.isSelected()) {
                    TurkishTranslateColumn.setVisible(false);
                    wordsTurkishTranslationCheckBoxFlag = false;
                }
                else {
                    TurkishTranslateColumn.setVisible(true);
                    wordsTurkishTranslationCheckBoxFlag = true;
                }
            }
        });


        CheckBox exampleSentenceCheckBox = new CheckBox("Example Sentence");
        exampleSentenceCheckBox.setSelected(exampleSentenceCheckBoxFlag);

        exampleSentenceCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!exampleSentenceCheckBox.isSelected()) {
                    exampleSentenceColumn.setVisible(false);
                    exampleSentenceCheckBoxFlag = false;
                }
                else if(exampleSentenceCheckBox.isSelected()) {
                    exampleSentenceColumn.setVisible(true);
                    exampleSentenceCheckBoxFlag = true;
                }
            }
        });


        hBoxTop.getChildren().addAll(wordCheckBox, wordsEnglishDescriptionCheckBox, wordsTurkishTranslationCheckBox,
                exampleSentenceCheckBox);
        hBoxTop.setSpacing(80);
        hBoxTop.setPadding(new Insets(15, 12, 15, 70));

        borderPane.setTop(hBoxTop);
    }

    public void fillTheTable(BorderPane borderPane, Option option){

        borderPane.setCenter(null);

        wordColumn = new TableColumn<>("Word");
        wordColumn.setMinWidth(100);
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        wordColumn.setVisible(wordCheckBoxFlag);

        engDescColumn = new TableColumn<>("Word's English Description");
        engDescColumn.setMinWidth(300);
        engDescColumn.setCellValueFactory(new PropertyValueFactory<>("wordsEnglishDescription"));
        engDescColumn.setVisible(wordsEnglishDescriptionCheckBoxFlag);

        TurkishTranslateColumn = new TableColumn<>("Word's Turkish Translation");
        TurkishTranslateColumn.setMinWidth(300);
        TurkishTranslateColumn.setCellValueFactory(new PropertyValueFactory<>("wordsTurkishTranslation"));
        TurkishTranslateColumn.setVisible(wordsTurkishTranslationCheckBoxFlag);

        exampleSentenceColumn = new TableColumn<>("Example Sentence");
        exampleSentenceColumn.setMinWidth(1000);
        exampleSentenceColumn.setCellValueFactory(new PropertyValueFactory<>("exampleSentence"));
        exampleSentenceColumn.setVisible(exampleSentenceCheckBoxFlag);

        table = new TableView<>();
        if(option == Option.ALL)
            table.setItems(getListOfObjects());
        else if(option == Option.RANDOM)
            table.setItems(getARandomObject());
        table.getColumns().addAll(wordColumn, engDescColumn, TurkishTranslateColumn, exampleSentenceColumn);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(table);

        borderPane.setCenter(vbox);
    }

    public ObservableList<Dictionary> getARandomObject(){


        ObservableList<Dictionary> randomObject;

        if(list!=null && list.size()>0) {
            Random random = new Random();
            int randomNum = random.nextInt(list.size());

            randomObject = FXCollections.observableArrayList();
            randomObject.add(list.get(randomNum));
        }
        else
            randomObject = getListOfObjects();


        return randomObject;
    }

    public void saveTheWord(Dictionary obj){

        table.getItems().add(obj);

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