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
}
