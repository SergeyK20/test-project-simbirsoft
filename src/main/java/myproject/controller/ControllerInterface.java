package myproject.controller;

import java.io.IOException;
import java.sql.SQLException;


public interface ControllerInterface<T> {
    void startController(T t) throws IOException, SQLException;
}
