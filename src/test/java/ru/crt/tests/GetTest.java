package ru.crt.tests;

import org.junit.jupiter.api.Test;
import ru.crt.dataHelper.Data;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class GetTest {
    static Data data = new Data();
    @Test
    //id=1
    void getBooks() {
        given()
                .baseUri(data.getBaseUri())
                .when()
                .get(data.getPath())
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("GetJsonSchema.json"));
    }

    @Test
    //id=2
    void getNonExistentBook() {
        String path = data.getPath() + "/" + data.getErrorId();
        given()
                .baseUri(data.getBaseUri())
                .when()
                .get(path)
                .then()
                .statusCode(404)
                .body("error", equalTo(data.getErrorMessage(data.getErrorId())));
    }

    @Test
    //id=3
    void getBookById() {
        String path = data.getPath() + "/" + data.getId();
        given()
                .baseUri(data.getBaseUri())
                .when()
                .get(path)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("GetByIdJsonSchema.json"));
    }
}
