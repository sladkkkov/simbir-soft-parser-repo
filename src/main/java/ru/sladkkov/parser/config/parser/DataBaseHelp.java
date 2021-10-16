package ru.sladkkov.parser.config.parser;

import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static ru.sladkkov.parser.config.parser.Parser.parsePage;

@Log4j
public class DataBaseHelp {

    private final JdbcTemplate jdbcTemplate;

    public DataBaseHelp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static JdbcTemplate databaseAccess() {
        log.info("Database Access Request");

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(org.postgresql.Driver.class);

        dataSource.setUrl("jdbc:postgresql://34.88.62.103:5432/SimbirSoftParser");
        dataSource.setPassword("Danila1q2w3e");
        dataSource.setUsername("postgres");

        log.info("Access received");

        return new JdbcTemplate(dataSource);
    }

    // Получить все строки из БД, отсортированные по количеству повторенний
    public List<Word> getAllWords() {
        return jdbcTemplate.query("SELECT * FROM Word ORDER BY quantity DESC ", new WordMapper());
    }

    //Запрос на получение строки с БД по ключу (word). Если ключа не будет вернётся null
    public Word getWord(String s) {
        return jdbcTemplate.query("SELECT * FROM Word WHERE word_name=?", new Object[]{s}, new WordMapper())
                .stream().findAny().orElse(null);
    }

    // Удаляем старую БД, если она существует, и создаем пустую с полями word_name и quantity
    public void newDataBase() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS Word;" +
                "create table Word(word_name varchar(35), quantity integer)");
    }

    //Сохранить экземпляр word в БД.
    public void save(Word word) {
        jdbcTemplate.update("INSERT INTO Word VALUES (?,?)", word.getWordName(), word.getQuantity());
    }

    //Изменить количество повторенний переданного слова в БД.
    public void update(Word updWord) {
        jdbcTemplate.update("UPDATE Word SET  quantity = ? WHERE word_name = ?", updWord.getQuantity(),
                updWord.getWordName());
    }

    public void countUniqueWords() {
        String siteText = null;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ссылку на произвольный HTML сайт");
        //Обрабатываем IOException при неккоректном вводе ссылки в консоли
        try {
            log.info("Requesting access to the site");

            siteText = parsePage(
                    scanner.nextLine()).text();  // Парсим при помощи jsoup текст со страницы

            log.info("Access to the site is allowed");

        } catch (IOException e) {
            log.error("Access to the site has not been received ", e);
        }

        String[] array = new String[0];

        // Исправляем регистр и избавляемся от мусора, формируем массив слов.
        if (siteText != null) {
            siteText = siteText.toLowerCase(Locale.ROOT);
            array = siteText.split("[^а-яa-z]+");
        }

        // Сформируем финальный список уникальных слов в мапу, и потом занесем в БД. В 3 раза быстрее 2 варианта, но всё ещё очень долго.
        HashMap<String, Word> uniqueWord = new HashMap<>();
        for (String s : array) {
            int i = 1;
            if (!uniqueWord.containsKey(s)) {
                uniqueWord.put(s, new Word(s, i));
                i++;
            } else {
                uniqueWord.replace(s, new Word(s, uniqueWord.get(s).getQuantity() + 1));
            }
        }
        // Добавление всех записей в БД
        log.info("Insert words into database started");

        uniqueWord.forEach((s, word) -> {
            save(word);
            log.debug("Saving " + word.getWordName() + " - " + word.getQuantity());
        });

        log.info("Insert words into database ended");

    /*   Сразу добавляем запись [wordName, quantity] в БД. Обработка SQL инсертов и апдейтов в 3 раза дольше.
      for (String s : array) {
      int i = 1;
      if (getWord(s) == null) {
        save(new Word(s, i));
        i++;
      } else {
        update(new Word(s, getWord(s).getQuantity() + 1));
      }
    }*/
    }
}
