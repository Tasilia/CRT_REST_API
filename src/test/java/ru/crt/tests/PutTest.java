package ru.crt.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.crt.dataHelper.Book;
import ru.crt.dataHelper.Data;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class PutTest {
    static Data data = new Data();
    Book updatedBook = data.getUpdatedBook();
    static int id;
    String path = data.getPath() + "/" + id;

    @BeforeAll
    static void postBook() {
        id =
                given()
                        .baseUri(data.getBaseUri())
                        .contentType("application/json")
                        .body(data.getBook())
                        .when()
                        .post(data.getPath())
                        .then()
                        .extract()
                        .path("book.id");
    }

    @Test
    //id=19
    void putNonExistentBook() {
        String errorPath = data.getPath() + "/" + data.getErrorId();
        given()
                .baseUri(data.getBaseUri())
                .when()
                .put(errorPath)
                .then()
                .statusCode(404)
                .body("error", equalTo(data.getErrorMessage(data.getErrorId())));
    }

    @Test
    //id=20
    void putBookWithoutAuthor() {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"name\": \"" + updatedBook.getName() + "\"}")
                .when()
                .put(path)
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getRequiredAuthorError()));
    }

    @Test
    //id=21
    void putBookWithoutName() {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"author\": \"" + updatedBook.getAuthor() + "\"}")
                .when()
                .put(path)
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getRequiredNameError()));
    }

    @Test
    //id=22
    void putBookWithoutYear() {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"name\": \"" + updatedBook.getName() + "\", \"author\": \"" + updatedBook.getAuthor() + "\"}")
                .when()
                .put(path)
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getRequiredYearError()));
    }

    @Test
    //id=23
    void putBookWithoutIsElectronicBook() {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"name\": \"" + updatedBook.getName() + "\", \"author\": \"" + updatedBook.getAuthor() +
                        "\", \"year\": " + updatedBook.getYear() + "}")
                .when()
                .put(path)
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getRequiredIsElectronicBookError()));
    }

    @Test
    //id=24
    void putABook() {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body(updatedBook)
                .when()
                .put(path)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("GetByIdJsonSchema.json"))
                .body("book.id", equalTo(id))
                .body("book.name", equalTo(updatedBook.getName()))
                .body("book.author", equalTo(updatedBook.getAuthor()))
                .body("book.year", equalTo(updatedBook.getYear()))
                .body("book.isElectronicBook", equalTo(updatedBook.getIsElectronicBook()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"\"", "1", "null", "true"})
    //id=25
    void putBookWithWrongName(String name) {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"name\": " + name + "}")
                .when()
                .put(path)
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getNameErrorMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "null", "true"})
    //id=26
    void putBookWithWrongAuthor(String author) {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"name\": \"" + updatedBook.getName() + "\", \"author\": " + author + "}")
                .when()
                .put(path)
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getAuthorErrorMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"year\"", "null", "true"})
    //id=27
    void putBookWithWrongYear(String year) {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"name\": \"" + updatedBook.getName() + "\", \"author\": \"" + updatedBook.getAuthor() +
                        "\", \"year\": " + year + "}")
                .when()
                .put(path)
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getYearErrorMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"isElectronicBook\"", "null", "1"})
    //id=28
    void putBookWithWrongIsElectronicBook(String isElectronicBook) {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"name\": \"" + updatedBook.getName() + "\", \"author\": \"" + updatedBook.getAuthor() +
                        "\", \"year\": " + updatedBook.getYear() + ", \"isElectronicBook\": " + isElectronicBook + "}")
                .when()
                .put(path)
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getIsElectronicBookErrorMessage()));
    }

    @Test
    //id=29
    void putBookWithoutJsonBody() {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{}")
                .when()
                .put(path)
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getJsonError()));
    }

    @Test
    //id=30
    void getAfterPutBook() {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body(updatedBook)
                .when()
                .put(path);
        given()
                .baseUri(data.getBaseUri())
                .when()
                .get(data.getPath())
                .then()
                .body("books[-1].id", equalTo(id))
                .body("books[-1].name", equalTo(updatedBook.getName()))
                .body("books[-1].author", equalTo(updatedBook.getAuthor()))
                .body("books[-1].year", equalTo(updatedBook.getYear()))
                .body("books[-1].isElectronicBook", equalTo(updatedBook.getIsElectronicBook()));
    }

    @Test
    //id=31
    void getModifiedBook() {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body(updatedBook)
                .when()
                .put(path);
        given()
                .baseUri(data.getBaseUri())
                .when()
                .get(path)
                .then()
                .body("book.id", equalTo(id))
                .body("book.name", equalTo(updatedBook.getName()))
                .body("book.author", equalTo(updatedBook.getAuthor()))
                .body("book.year", equalTo(updatedBook.getYear()))
                .body("book.isElectronicBook", equalTo(updatedBook.getIsElectronicBook()));
    }
}
