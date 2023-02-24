package ru.crt.dataHelper;

public class Data {
    String baseUri = "http://localhost:5000";
    String path = "/api/books";
    String errorId = "0";
    String nameErrorMessage = "Name must be String type (Unicode)";
    String authorErrorMessage = "Author must be String type (Unicode)";
    String yearErrorMessage = "Year must be Int type";
    String isElectronicBookErrorMessage = "IsElectronicBook must be Bool type";
    String jsonError = "Not found request Json body";
    String name = "Граф Монте-Кристо";
    int id = 1;
    int year = 1846;
    String author = "Александр Дюма";
    Boolean isElectronicBook = true;
    int updatedYear = 1862;
    String updatedName = "Отверженные";
    String updatedAuthor = "Виктор Гюго";
    Boolean updatedIsElectronicBook = false;
    String requiredNameError = "Name is required";

    String requiredAuthorError = "Author is required";
    String requiredYearError = "Year is required";
    String requiredIsElectronicBookError = "IsElectronicBook is required";

    public Book getBook() {
        return new Book(name, id, year, author, isElectronicBook);
    }

    public Book getUpdatedBook() {
        return new Book(updatedName, id, updatedYear, updatedAuthor, updatedIsElectronicBook);
    }

    public String getBaseUri() {
        return baseUri;
    }

    public String getPath() {
        return path;
    }

    public String getErrorId() {
        return errorId;
    }

    public String getErrorMessage(String id) {
        return "Book with id " + id + " not found";
    }

    public int getId() {
        return id;
    }

    public String getNameErrorMessage() {
        return nameErrorMessage;
    }

    public String getRequiredNameError() {
        return requiredNameError;
    }

    public String getRequiredAuthorError() {
        return requiredAuthorError;
    }

    public String getRequiredYearError() {
        return requiredYearError;
    }

    public String getRequiredIsElectronicBookError() {
        return requiredIsElectronicBookError;
    }

    public String getAuthorErrorMessage() {
        return authorErrorMessage;
    }

    public String getYearErrorMessage() {
        return yearErrorMessage;
    }

    public String getIsElectronicBookErrorMessage() {
        return isElectronicBookErrorMessage;
    }

    public String getJsonError() {
        return jsonError;
    }

}
