package ask.dictionary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by selimk on 3/10/2017.
 */

@Entity
public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    private String word;
    private String wordsEnglishDescription;
    private String wordsTurkishTranslation;
    private String exampleSentence;

    public int getId() {
        return Id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordsEnglishDescription() {
        return wordsEnglishDescription;
    }

    public void setWordsEnglishDescription(String wordsEnglishDescription) {
        this.wordsEnglishDescription = wordsEnglishDescription;
    }

    public String getWordsTurkishTranslation() {
        return wordsTurkishTranslation;
    }

    public void setWordsTurkishTranslation(String wordsTurkishTranslation) {
        this.wordsTurkishTranslation = wordsTurkishTranslation;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    public void setExampleSentence(String exampleSentence) {
        this.exampleSentence = exampleSentence;
    }
}
