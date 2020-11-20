package myproject;

import myproject.view.View;

/**
 * @author Kashkinov Sergey
 */
public class App {

    /**
     * Начало работы программы. Запускаем представление нашей программы.
     *
     * @param args параметры командной строки
     */
    public static void main(String[] args) {
        View view = new View();
        view.start();
    }
}
