package ask.dictionary;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Created by selimk on 3/10/2017.
 */
public class MainApp extends Application {

    private static ObservableList<Dictionary> list;
    private static HashMap<String, Integer> randomMap;

    private TableView<Dictionary> table;

    private TableColumn<Dictionary, String> wordColumn;
    private TableColumn<Dictionary, String> engDescColumn;
    private TableColumn<Dictionary, String> TurkishTranslateColumn;
    private TableColumn<Dictionary, String> exampleSentenceColumn;

    private static boolean wordCheckBoxFlag;
    private static boolean wordsEnglishDescriptionCheckBoxFlag;
    private static boolean wordsTurkishTranslationCheckBoxFlag;
    private static boolean exampleSentenceCheckBoxFlag;

    static {
        wordCheckBoxFlag = true;
        wordsEnglishDescriptionCheckBoxFlag = true;
        wordsTurkishTranslationCheckBoxFlag = true;
        exampleSentenceCheckBoxFlag = true;

        fillList();

        randomMap = new HashMap<>();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();

        VBox vBoxBottom = new VBox();

        HBox hBoxSearch = new HBox();

        TextField searchField = new TextField();
        searchField.setPromptText("Write the word that you want to find...");
        searchField.setPrefWidth(searchField.getPromptText().length()*6);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> {
            String text = searchField.getText().trim().toLowerCase();
            ObservableList<Dictionary> searchList = FXCollections.observableArrayList();
            searchList.addAll(list.stream().filter(dict -> text.equalsIgnoreCase(dict.getWord()) || dict.getWord().contains(text)).collect(Collectors.toList()));
            table.setItems(searchList);
        });

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            searchField.clear();
            initializeCheckBoxes(borderPane);
            fillTheTable(borderPane, Option.ALL);
        });

        hBoxSearch.getChildren().addAll(searchField, searchButton, clearButton);
        hBoxSearch.setPadding(new Insets(30,100,20,200));
        hBoxSearch.setSpacing(30);
        hBoxSearch.setVisible(false);

        HBox hBoxButtons = new HBox();

        Button addButton = new Button("Add a new word"), randomButton = new Button("Get a random word"),
                showAllButton = new Button("Show all words"), deleteButton= new Button("Delete");

        addButton.managedProperty().bind(addButton.visibleProperty());
        addButton.setOnAction(e -> {
            hBoxSearch.setVisible(false);
            addNewWord(borderPane);
            deleteButton.setVisible(false);
            hBoxButtons.setPadding(new Insets(15, 12, 70, 200));
            addButton.setVisible(false);
            randomButton.setVisible(true);
            showAllButton.setVisible(true);
        });

        randomButton.managedProperty().bind(randomButton.visibleProperty());
        randomButton.setOnAction(e -> {
            initializeCheckBoxes(borderPane);
            fillTheTable(borderPane, Option.RANDOM);
            hBoxSearch.setVisible(false);
            addButton.setVisible(true);
            showAllButton.setVisible(true);
            deleteButton.setVisible(true);
            hBoxButtons.setPadding(new Insets(15, 12, 70, 70));
        });

        showAllButton.managedProperty().bind(showAllButton.visibleProperty());
        showAllButton.setOnAction(e -> {
            initializeCheckBoxes(borderPane);
            fillTheTable(borderPane, Option.ALL);
            hBoxSearch.setVisible(true);
            addButton.setVisible(true);
            randomButton.setVisible(true);
            showAllButton.setVisible(false);
            deleteButton.setVisible(true);
            hBoxButtons.setPadding(new Insets(15, 12, 50, 140));
            hBoxButtons.setSpacing(100);
        });

        deleteButton.setOnAction(e -> deleteAnObject());
        deleteButton.setVisible(false);
        deleteButton.disableProperty().bind(Bindings.isEmpty(list));


        hBoxButtons.setSpacing(100);
        hBoxButtons.setPadding(new Insets(15, 12, 70, 120));//(top/right/bottom/left)
        hBoxButtons.getChildren().addAll(addButton,randomButton,showAllButton, deleteButton);

        vBoxBottom.getChildren().addAll(hBoxSearch, hBoxButtons);
        vBoxBottom.setSpacing(15);

        borderPane.setBottom(vBoxBottom);

        Scene scene = new Scene(borderPane,800,600);

        stage.setTitle("Dictionary App");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteAnObject(){
        ObservableList<Dictionary> selectedWords, allWords;

        allWords = table.getItems();
        selectedWords = table.getSelectionModel().getSelectedItems();

        if(selectedWords.size()>0){
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


        Button saveButton = new Button("Save");

        saveButton.disableProperty().bind(wordTextFieldValid.not().or(TurkishTranslateTextFieldValid.not()));

        saveButton.setOnAction(e -> {
            Dictionary recordedObject = new Dictionary();

            recordedObject.setWord(wordTextField.getText());
            recordedObject.setWordsEnglishDescription(engDescTextField.getText());
            recordedObject.setWordsTurkishTranslation(TurkishTranslateTextField.getText());
            recordedObject.setExampleSentence(exampleSentenceTextField.getText());

            wordTextField.clear();
            engDescTextField.clear();
            TurkishTranslateTextField.clear();
            exampleSentenceTextField.clear();

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

        wordCheckBox.setOnAction(event -> {
            if(!wordCheckBox.isSelected()) {
                wordColumn.setVisible(false);
                wordCheckBoxFlag = false;
            }
            else{
                wordCheckBoxFlag = true;
                wordColumn.setVisible(true);
            }
        });


        CheckBox wordsEnglishDescriptionCheckBox = new CheckBox("English Description");
        wordsEnglishDescriptionCheckBox.setSelected(wordsEnglishDescriptionCheckBoxFlag);

        wordsEnglishDescriptionCheckBox.setOnAction(event -> {
            if(wordsEnglishDescriptionCheckBox.isSelected()) {
                engDescColumn.setVisible(true);
                wordsEnglishDescriptionCheckBoxFlag = true;
            }
            else{
                engDescColumn.setVisible(false);
                wordsEnglishDescriptionCheckBoxFlag = false;
            }
        });


        CheckBox wordsTurkishTranslationCheckBox = new CheckBox("Turkish Translation");
        wordsTurkishTranslationCheckBox.setSelected(wordsTurkishTranslationCheckBoxFlag);

        wordsTurkishTranslationCheckBox.setOnAction(event -> {
            if(!wordsTurkishTranslationCheckBox.isSelected()) {
                TurkishTranslateColumn.setVisible(false);
                wordsTurkishTranslationCheckBoxFlag = false;
            }
            else {
                TurkishTranslateColumn.setVisible(true);
                wordsTurkishTranslationCheckBoxFlag = true;
            }
        });


        CheckBox exampleSentenceCheckBox = new CheckBox("Example Sentence");
        exampleSentenceCheckBox.setSelected(exampleSentenceCheckBoxFlag);

        exampleSentenceCheckBox.setOnAction(event -> {
            if(!exampleSentenceCheckBox.isSelected()) {
                exampleSentenceColumn.setVisible(false);
                exampleSentenceCheckBoxFlag = false;
            }
            else if(exampleSentenceCheckBox.isSelected()) {
                exampleSentenceColumn.setVisible(true);
                exampleSentenceCheckBoxFlag = true;
            }
        });

        Label wordCount = new Label("Words: " + list.size());

        hBoxTop.getChildren().addAll(wordCheckBox, wordsEnglishDescriptionCheckBox, wordsTurkishTranslationCheckBox,
                exampleSentenceCheckBox, wordCount);
        hBoxTop.setSpacing(60);
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
        randomObject = FXCollections.observableArrayList();

        if(list!=null && list.size()>0) {
            Random random = new Random();
            int randomNum = random.nextInt(list.size());


            Dictionary obj = list.get(randomNum);
            String randomText = obj.getWord();

            System.out.println(randomText);

            if(randomMap.get(randomText) == null) {
                randomObject.add(obj);
                randomMap.put(randomText, 1);
                return randomObject;
            }

            else if(randomMap.get(randomText) == 0) {
                randomObject.add(obj);
                randomMap.put(randomText, 1);
                return randomObject;
            }

            else if(randomMap.get(randomText) == 1) {
                randomMap.put(randomText, 2);
                return getARandomObject();
            }

            else {
                randomMap.put(randomText, 0);
                return getARandomObject();
            }
        }
        else
            return getListOfObjects();
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