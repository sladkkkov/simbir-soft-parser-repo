package ru.sladkkov.parser.config.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

public class Parser {

    protected static Document parsePage(String link) throws IOException {
        return Jsoup.parse(new URL(link), 5000);
    }

}