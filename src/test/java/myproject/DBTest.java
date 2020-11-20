package myproject;

import myproject.model.WordsOnThePage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class DBTest {
    EntityManager entityManager;

    /**
     * Тест на подключение к JPA
     */
    @Before
    public void shouldStartHibernate() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestProject");
        entityManager = emf.createEntityManager();
    }

    /**
     * Тест сохранение объейта в Бд
     */
    @Test
    public void shouldSaveInDB() {
        WordsOnThePage wordsOnThePage = new WordsOnThePage();
        wordsOnThePage.setNamePage("www.test2.com.html");
        Map<String, Integer> countWords = new HashMap<String, Integer>();
        countWords.put("ПРИВЕТ", 3);
        countWords.put("ЗДРАВСВУЙ", 5);
        wordsOnThePage.setCountWords(countWords);
        entityManager.getTransaction().begin();
        entityManager.persist(wordsOnThePage);
        entityManager.getTransaction().commit();
    }

    @After
    public void shouldCloseHibernate() {
        entityManager.close();
    }

}
