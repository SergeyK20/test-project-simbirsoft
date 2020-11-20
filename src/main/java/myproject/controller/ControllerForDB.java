package myproject.controller;

import myproject.connection.Connection;
import myproject.dao.WordsCountsDAO;
import myproject.model.WordsOnThePage;

import java.sql.SQLException;
import java.util.Map;

/**
 * Класс занимающийся  работой с БД
 */
public class ControllerForDB {

    private WordsCountsDAO wordsCountsDAO;

    public ControllerForDB() {
        wordsCountsDAO = new WordsCountsDAO(Connection.getConnection());
    }

    /**
     * Метод сохраняющий значиня параметров в БД
     *
     * @param namePage   название файла со страницей HTML
     * @param countWords подсчет слов вхождения
     */
    public void saveWords(String namePage, Map<String, Integer> countWords) throws SQLException {
        WordsOnThePage wordsOnThePage = new WordsOnThePage();
        wordsOnThePage.setNamePage(namePage);
        wordsOnThePage.setCountWords(countWords);
        wordsCountsDAO.create(wordsOnThePage);
    }

    /**
     * Метод поиска названия файла в Бд
     *
     * @param namePage название фойла
     * @return возращает true файла нет, false - если есть
     */
    public boolean findPageHTML(String namePage) {
        return wordsCountsDAO.find(namePage).equals("");
    }

}
