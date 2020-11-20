package myproject.controller;

import myproject.view.View;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Класс котроллер, выполняющий установление соединения с сервером по URL
 * получает HTML страницу, сохраняет ее в файл и анализирует файл.
 * После анализа заносит название файла и список слов вхождения в БД.
 */
public class Controller implements ControllerInterface<String> {

    private View view;
    private ControllerForDB controllerForDB;

    public Controller(View view) {
        this.view = view;
        controllerForDB = new ControllerForDB();
    }

    public void startController(String adrURL) throws IOException, SQLException {
        HttpURLConnection connection = connectionToServer(adrURL);
        view.getMessage("Соединение успешно установилось...");
        String nameFile = saveHTMLInTheFile(connection, adrURL);
        view.getMessage("Файл с HTML страницой успешно сохранился...");
        if (!controllerForDB.findPageHTML(nameFile)) {
            throw new SQLException("Сайт под таким URL уже проанализирован...");
        }
        Map<String, Integer> countWords = countingOccurrenceOfWords(nameFile);
        view.getListWords(countWords);
        controllerForDB.saveWords(nameFile, countWords);
    }

    private HttpURLConnection connectionToServer(String adrURL) throws IOException {
        try {
            URL url = new URL(adrURL);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new IOException("Невозможно установить соединение...");
        }
    }

    private String saveHTMLInTheFile(HttpURLConnection connection, String adrURL) throws IOException {
        Pattern deleteSubstringOfURL = Pattern.compile("https://|http://|/$");
        String nameFile = deleteSubstringOfURL.matcher(adrURL).replaceAll("") + ".html";
        try (BufferedReader readOfStream = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
             FileWriter fileWithHTMLPage = new FileWriter(nameFile);
             BufferedWriter writeInFile = new BufferedWriter(fileWithHTMLPage)
        ) {
            String line = "";
            while ((line = readOfStream.readLine()) != null) {
                writeInFile.write(line + "\n");
            }
        } catch (IOException e) {
            throw new IOException("Невозможно сохранить файл на диск...");
        }
        return nameFile;
    }

    public Map<String, Integer> countingOccurrenceOfWords(String nameFile) throws IOException {
        Path pathToTheHTMLFile = Paths.get(nameFile);
        boolean flagStart = false;
        boolean flagEnd = false;
        boolean flagScript = false;
        Pattern startTag = Pattern.compile("<");
        Pattern endTag = Pattern.compile("/>");
        Pattern endTagSecondVersion = Pattern.compile(">");
        Pattern startTagScript = Pattern.compile("<script|<SCRIPT|<style|<head>");
        Pattern endTagScript = Pattern.compile("</script|</SCRIPT|</style|</head");
        Pattern startOrEndTag = Pattern.compile("<|>");
        Pattern unnecessaryCharacters = Pattern.compile("[,//.\\s!?\";:\\[\\]()\\n\\r\\t]+");
        Scanner readInTheFile = new Scanner(pathToTheHTMLFile);
        String word;
        StringBuilder unprocessedString = new StringBuilder();
        String wordResult;

        Map<String, Integer> mapWords = new HashMap<>();
        int i = 0;
        while (readInTheFile.hasNext()) {
            unprocessedString.append(readInTheFile.next());
            while (/*(startOrEndTag.matcher(unprocessedString.toString()).find()) || (*/!unprocessedString.toString().equals("")/*)*/) {
                word = findWord(unprocessedString);
                // если есть шаблон со скриптом
                if (startTagScript.matcher(word).find()) {
                    i++;
                    flagScript = true;
                }
                if (!flagScript) {
                    if (!flagEnd) {
                        if (!flagStart) {
                            // если есть шаблон с <
                            if (startTag.matcher(word).find()) {
                                flagStart = true;
                            }
                        } else {
                            //ищем конец унарного тега
                            if ((endTag.matcher(word)).find()) {
                                flagStart = false;
                            } else {
                                // конец обычного тега
                                if ((endTagSecondVersion.matcher(word)).find()) {
                                    flagEnd = true;
                                    flagStart = false;
                                }
                            }
                        }
                    } else {
                        if (startTag.matcher(word).find()) {
                            flagEnd = false;
                            flagStart = true;
                        } else {
                            wordResult = unnecessaryCharacters.matcher(word).replaceAll("").toUpperCase();
                            wordResult = wordResult.replaceAll("Ё", "Е");
                            // если осталась пустая строка, то не записываем
                            if (!wordResult.equals("")) {
                                if ((mapWords.size() != 0) && (mapWords.containsKey(wordResult))) {
                                    mapWords.put(wordResult, mapWords.get(wordResult) + 1);
                                } else {
                                    mapWords.put(wordResult, 1);
                                }
                            }
                        }
                    }
                } else {
                    if (endTagScript.matcher(word).matches()) {
                        i--;
                        if (i == 0) {
                            flagScript = false;
                        }
                    }
                }
            }
        }
        return mapWords;
    }

    private String findWord(StringBuilder string) {
        String result;
        // анализировать строку где есть <
        if (string.toString().contains("<")) {
            // анализировать строку где есть < >
            if (string.toString().contains(">")) {
                if (string.toString().indexOf("<") < string.toString().indexOf(">")) {
                    if (string.toString().indexOf("<") != 0) {
                        result = string.toString().substring(0, string.toString().indexOf("<"));
                        string.replace(0, string.length(), string.substring(string.toString().indexOf("<")));
                    } else {
                        result = string.toString().substring(0, string.toString().indexOf(">"));
                        string.replace(0, string.length(), string.substring(string.toString().indexOf(">")));
                    }
                    return result;
                } else {
                    //возвращает подстроку с >, если есть < и >
                    result = string.toString().substring(0, string.toString().indexOf(">") + 1);
                    string.replace(0, string.length(), string.substring(string.toString().indexOf(">") + 1));
                    return result;
                }
            } else {
                //возвращает подстроку с >
                if (string.toString().indexOf("<") != 0) {
                    result = string.substring(0, string.toString().indexOf("<"));
                    string.replace(0, string.length(), string.substring(string.toString().indexOf("<")));
                } else {
                    result = string.toString();
                    string.replace(0, string.length(), "");
                }
                return result;
            }
        } else {
            if (!string.toString().contains(">")) {
                result = string.toString();
                string.replace(0, string.length(), "");
            } else {
                //возвращает подстроку с >
                result = string.substring(0, string.toString().indexOf(">") + 1);
                string.replace(0, string.length(), string.substring(string.indexOf(">") + 1));
            }
            return result;
        }

    }
}
