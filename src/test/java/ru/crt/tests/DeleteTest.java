package ru.crt.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.crt.dataHelper.Book;
import ru.crt.dataHelper.Data;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteTest {
    static Data data = new Data();
    static Book book = data.getBook();
    static ArrayList<String> ids = new ArrayList<>(3);

    @BeforeAll
    static void postBooks() {
        for (int i = 0; i < 3; i++) {
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
            ids.add(Integer.toString(id));
        }
    }

    @Test
        //id=4
    void deleteANonExistentBook() {
        String path = data.getPath() + "/" + data.getErrorId();
        given()
                .baseUri(data.getBaseUri())
                .when()
                .delete(path)
                .then()
                .statusCode(404)
                .body("error", equalTo(data.getErrorMessage(data.getErrorId())));
    }

    @Test
        //id=5
    void deleteBook() {
        String path = data.getPath() + "/" + ids.get(0);
        given()
                .baseUri(data.getBaseUri())
                .when()
                .delete(path)
                .then()
                .statusCode(200)
                .body("result", equalTo(true));
    }

    @Test
        //id=6
    void getAfterDeleteBook() {
        ArrayList body =
                given()
                        .baseUri(data.getBaseUri())
                        .when()
                        .get(data.getPath())
                        .then()
                        .extract()
                        .path("books");
        int size = body.size();
        String path = data.getPath() + "/" + ids.get(1);
        given()
                .baseUri(data.getBaseUri())
                .when()
                .delete(path);
        ArrayList bodyAfterDelete =
                given()
                        .baseUri(data.getBaseUri())
                        .when()
                        .get(data.getPath())
                        .then()
                        .extract()
                        .path("books");
        int sizeAfterPost = bodyAfterDelete.size();
        Assertions.assertEquals(1, size - sizeAfterPost);
    }

    @Test
        //id=7
    void getDeletedBook() {
        String path = data.getPath() + "/" + ids.get(2);
        given()
                .baseUri(data.getBaseUri())
                .when()
                .delete(path);
        given()
                .baseUri(data.getBaseUri())
                .when()
                .get(path)
                .then()
                .statusCode(404)
                .body("error", equalTo(data.getErrorMessage(ids.get(2))));

    }

    @Test
        //id=8
    void addBookAfterDeleteBook() {
        ArrayList<String> ids = new ArrayList<>(3);
        for (int i = 0; i < 2; i++) {
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
            ids.add(Integer.toString(id));
        }
        String path = data.getPath() + "/" + ids.get(0);
        given()
                .baseUri(data.getBaseUri())
                .when()
                .delete(path);
        given()
                .baseUri(data.getBaseUri())
                .contentType("application/json")
                .body(book)
                .when()
                .post(data.getPath())
                .then()
                .body("book.id", equalTo(Integer.parseInt(ids.get(1)) + 1));

    }
}
