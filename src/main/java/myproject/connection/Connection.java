package myproject.connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Класс Singleton, для того чтобы только один оюъект осуществлял подключение и работу с Бд
 */
public class Connection {

    private static EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("TestProject");
    }

    public static EntityManager getConnection() {
        return entityManagerFactory.createEntityManager();
    }

}
