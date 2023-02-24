package ru.crt.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.crt.dataHelper.Book;
import ru.crt.dataHelper.Data;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class PostTest {
    Data data = new Data();
    Book book = data.getBook();

    @Test
    //id=9
    void getAfterPostBook() {
        ArrayList body =
                given()
                        .baseUri(data.getBaseUri())
                        .when()
                        .get(data.getPath())
                        .then()
                        .extract()
                        .path("books");
        int size = body.size();
        String lastBook = body.get(size-1).toString();
        String id = lastBook.substring(lastBook.indexOf("id")+3, lastBook.indexOf(",",lastBook.indexOf("id")));
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body(book)
                .when()
                .post(data.getPath());
        ArrayList bodyAfterPost =
                given()
                        .baseUri(data.getBaseUri())
                        .when()
                        .get(data.getPath())
                        .then()
                        .body("books[-1].id", equalTo(Integer.parseInt(id)+1))
                        .body("books[-1].name", equalTo(book.getName()))
                        .body("books[-1].author", equalTo(book.getAuthor()))
                        .body("books[-1].year", equalTo(book.getYear()))
                        .body("books[-1].isElectronicBook", equalTo(book.getIsElectronicBook()))
                        .extract()
                        .path("books");
        int sizeAfterPost = bodyAfterPost.size();
        Assertions.assertEquals(1, sizeAfterPost - size);
    }
    @Test
    //id=10
    void getPostedBook() {
        int id =
            given()
                    .baseUri(data.getBaseUri())
                    .contentType("application/json")
                    .body(book)
                    .when()
                    .post(data.getPath())
                    .then()
                    .extract()
                    .path("book.id");
                given()
                        .baseUri(data.getBaseUri())
                        .when()
                        .get(data.getPath() + "/" + id)
                        .then()
                        .body("book.id", equalTo(id))
                        .body("book.name", equalTo(book.getName()))
                        .body("book.author", equalTo(book.getAuthor()))
                        .body("book.year", equalTo(book.getYear()))
                        .body("book.isElectronicBook", equalTo(book.getIsElectronicBook()));
    }

    @Test
    //id=11
    void postBook() {
        int lastId =
                given()
                        .baseUri(data.getBaseUri())
                        .when()
                        .get(data.getPath())
                        .then()
                        .extract()
                        .path("books[-1].id");
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body(book)
                .when()
                .post(data.getPath())
                .then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("GetByIdJsonSchema.json"))
                .body("book.author", equalTo(book.getAuthor()))
                .body("book.name", equalTo(book.getName()))
                .body("book.isElectronicBook", equalTo(book.getIsElectronicBook()))
                .body("book.year", equalTo(book.getYear()))
                .body("book.id", equalTo(lastId + 1));
    }

    @Test
    //id=12
    void postBookWithOnlyName() {
        int lastId =
                given()
                        .baseUri(data.getBaseUri())
                        .when()
                        .get(data.getPath())
                        .then()
                        .extract()
                        .path("books[-1].id");
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"name\": \"" + book.getName() + "\"}")
                .when()
                .post(data.getPath())
                .then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("GetByIdJsonSchema.json"))
                .body("book.author", equalTo(""))
                .body("book.name", equalTo(book.getName()))
                .body("book.isElectronicBook", equalTo(false))
                .body("book.year", equalTo(0))
                .body("book.id", equalTo(lastId + 1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"\"", "1", "null", "true"})
    //id=13
    void postBookWithWrongName(String name) {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"name\": " + name + "}")
                .when()
                .post(data.getPath())
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getNameErrorMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "null", "true"})
    //id=14
    void postBookWithWrongAuthor(String author) {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"name\": \"" + book.getName() + "\", \"author\": " + author + "}")
                .when()
                .post(data.getPath())
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getAuthorErrorMessage()));
        ;
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"year\"", "null", "true"})
    //id=15
    void postBookWithWrongYear(String year) {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"name\": \"" + book.getName() + "\", \"year\": " + year + "}")
                .when()
                .post(data.getPath())
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getYearErrorMessage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"isElectronicBook\"", "null", "1"})
    //id=16
    void postBookWithWrongIsElectronicBook(String isElectronicBook) {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"name\": \"" + book.getName() + "\", \"year\": " + isElectronicBook + "}")
                .when()
                .post(data.getPath())
                .then()
                .statusCode(400);
    }

    @Test
    //id=17
    void postBookWithoutAName() {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{\"author\": \"" + book.getAuthor() + "\"}")
                .when()
                .post(data.getPath())
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getRequiredNameError()));
    }

    @Test
    //id=18
    void postBookWithoutJsonBody() {
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body("{}")
                .when()
                .post(data.getPath())
                .then()
                .statusCode(400)
                .body("error", equalTo(data.getRequiredNameError()));
    }
}