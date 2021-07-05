package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class ExampleResourceTest {

    @Test
    @DisplayName("Valid call with original service")
    void testValidHelloEndpoint() {
        given()
                .when().get("/hello/valid")
                .then()
                .statusCode(200)
                .body(is("hello from service: ExampleDTO{code=1, description='a description', optional='something'}"));
    }

    @Test
    @DisplayName("Invalid call with original service")
    void testInvalidHelloEndpoint() {
        given()
                .when().get("/hello/invalid")
                .then()
                .statusCode(400)
                .body(is("{code=must not be null, description=must not be blank}"));
    }

}