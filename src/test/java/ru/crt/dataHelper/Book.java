package ru.crt.dataHelper;

public class Book {
    String name;
    int id;
    int year;
    String author;
    Boolean isElectronicBook;

    public Book(String name, int id, int year, String author, Boolean isElectronicBook) {
        this.name = name;
        this.id = id;
        this.year = year;
        this.author = author;
        this.isElectronicBook = isElectronicBook;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public String getAuthor() {
        return author;
    }

    public Boolean getIsElectronicBook() {
        return isElectronicBook;
    }
}
