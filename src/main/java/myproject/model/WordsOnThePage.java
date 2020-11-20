package myproject.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Класс Entity, который будет взаимодействовать с JPA после чего сохранять в реляционную БД
 */
@Entity
public class WordsOnThePage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String namePage;

    @ElementCollection
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id")
    private Map<String, Integer> countWords;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamePage() {
        return namePage;
    }

    public void setNamePage(String namePage) {
        this.namePage = namePage;
    }

    public Map<String, Integer> getCountWords() {
        return countWords;
    }

    public void setCountWords(Map<String, Integer> countWords) {
        this.countWords = countWords;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof WordsOnThePage)) {
            return false;
        }
        WordsOnThePage wordsOnThePage = (WordsOnThePage) o;
        return Objects.equals(namePage, wordsOnThePage.getNamePage());
    }

    public int hashCode() {
        return Objects.hash(namePage);
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Название файла: ")
                .append(namePage);
        for (Map.Entry<String, Integer> element : countWords.entrySet()) {
            str.append(element.getKey()).append(" ").append(element.getValue()).append("\n");
        }
        return str.toString();
    }

}
