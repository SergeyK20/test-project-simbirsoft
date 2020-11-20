package myproject.dao;

import myproject.model.WordsOnThePage;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.sql.SQLException;

/**
 * @author Kashkinov Sergey
 */
public class WordsCountsDAO implements AbstractDAO<WordsOnThePage, String> {

    private EntityManager entityManager;

    public WordsCountsDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(WordsOnThePage wordsOnThePage) throws SQLException {
        entityManager.getTransaction().begin();
        entityManager.persist(wordsOnThePage);
        entityManager.getTransaction().commit();
    }

    @Override
    public String find(String namePage) {
        try {
            return (String) entityManager.createNativeQuery("Select namePage from WordsOnThePage where namePage = ?")
                    .setParameter(1, namePage).getSingleResult();
        } catch (NoResultException e) {
            return "";
        }
    }
}
