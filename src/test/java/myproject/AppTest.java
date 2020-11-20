package myproject;

import myproject.controller.Controller;
import myproject.view.View;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class AppTest {

    private Controller controller = new Controller(new View());

    /**
     * Тест проверяющий ошибку валидации
     *
     * @throws IOException ошибка должна прийти
     */
    @Test(expected = IOException.class)
    public void shouldNotValidURL() throws IOException, SQLException {
        controller.startController("asdddasd");
    }

    /**
     * Тест проверяющий правильность валидации
     *
     * @throws SQLException может прийти ошибка если этот файл уже есть в БД
     */
    @Test
    public void shouldValidURL() throws IOException, SQLException {
        controller.startController("https://www.simbirsoft.com");
    }

    @Test
    public void shouldValidURLSecond() throws IOException, SQLException {
        controller.startController("https://www.netcracker.com");
    }

    @Test
    public void shouldFileAnalise() throws IOException {
        Map<String, Integer> map = controller.countingOccurrenceOfWords("www.simbirsoft.com.html");
        System.out.println(map);
    }

}
