package myproject.view;

import myproject.controller.Controller;
import myproject.controller.ControllerInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Kashkinov Sergey
 */
public class View {
    private ControllerInterface<String> controller;

    public View() {
        this.controller = new Controller(this);
    }

    public void start() {
        try (PrintWriter print = new PrintWriter(System.out, true);

             Scanner scnURL = new Scanner(System.in)) {

            print.println("Введите адрес web-страницы \n"
                    + "-------------Пример (https://www.simbirsoft.com/)---------------");

            String adrURL = scnURL.nextLine();
            controller.startController(adrURL);
        } catch (IOException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void getMessage(String message) {
        System.out.println(message);
    }

    public void getListWords(Map<String, Integer> mapWords) {
        for (Map.Entry<String, Integer> element : mapWords.entrySet()) {
            System.out.println(element.getKey() + " - " + element.getValue());
        }
    }


}
