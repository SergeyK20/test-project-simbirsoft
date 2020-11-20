package myproject.dao;

import java.sql.SQLException;

/**
 * @author Kashkinov Sergey
 */
public interface AbstractDAO<T, E> {
    void create(T t) throws SQLException;

    E find(E e) throws SQLException;
}
