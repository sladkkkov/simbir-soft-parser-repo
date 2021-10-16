package ru.sladkkov.parser.config.parser;

import lombok.extern.log4j.Log4j;

import static ru.sladkkov.parser.config.parser.DataBaseHelp.databaseAccess;

@Log4j
public class MainAppParser {

    public static void main(String[] args) {
        log.info("Parsing started");

        System.out.println("Check path C:\\LogFile\\logFile.log");

        DataBaseHelp dataBaseHelp = new DataBaseHelp(databaseAccess());

        dataBaseHelp.newDataBase();
        dataBaseHelp.countUniqueWords();

        if (!dataBaseHelp.getAllWords().isEmpty()) {
            System.out.println(dataBaseHelp.getAllWords());
        }

        log.info("Parsing ended");
    }
}
