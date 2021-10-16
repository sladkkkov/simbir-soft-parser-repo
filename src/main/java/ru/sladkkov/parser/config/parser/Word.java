package ru.sladkkov.parser.config.parser;

public class Word {

    private String wordName;
    private int quantity;

    public Word() {
    }

    public String getWordName() {
        return wordName;
    }

    @Override
    public String toString() {
        return wordName + " - " + quantity;
    }

    public Word(String wordName, int quantity) {
        this.wordName = wordName;
        this.quantity = quantity;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
