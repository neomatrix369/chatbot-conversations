package org.acme.getting.started;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class MessagingResourceTest {

    @Test
    public void testMessageWithSpacesEndpoint() {
        ValidatableResponse response = given()
                .when().get("/message/Hi Joe")
                .then()
                .statusCode(200)
                .body(is(notNullValue()));
    }
}