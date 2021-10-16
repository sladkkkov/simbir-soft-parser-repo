package ru.sladkkov.parser.config.parser;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WordMapper implements RowMapper<Word> {

    @Override
    public Word mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Word word = new Word();

        word.setWordName(resultSet.getString("word_name"));
        word.setQuantity(resultSet.getInt("quantity"));

        return word;
    }
}
