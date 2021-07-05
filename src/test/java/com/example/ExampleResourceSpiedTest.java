package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class ExampleResourceSpiedTest {

    @InjectSpy
    ExampleService service;

    @Test
    @DisplayName("Valid call with spied service and delegating to real method")
    void testValidHelloEndpointDelegating() {
        when(service.hello(any())).thenCallRealMethod();

        given()
                .when().get("/hello/valid")
                .then()
                .statusCode(200)
                .body(is("hello from service: ExampleDTO{code=1, description='a description', optional='something'}"));
    }

    @Test
    @DisplayName("Invalid call with spied service and delegating to real method")
    void testInvalidHelloEndpointDelegating() {
        when(service.hello(any())).thenCallRealMethod();

        given()
                .when().get("/hello/invalid")
                .then()
                .statusCode(400) // spy is causing validation to malfunction
                .body(is("{code=must not be null, description=must not be blank}"));
    }

    @Test
    @DisplayName("Valid call with spied service without delegating to real method")
    void testValidHelloEndpoint() {
        given()
                .when().get("/hello/valid")
                .then()
                .statusCode(200)
                .body(is("hello from service: ExampleDTO{code=1, description='a description', optional='something'}"));
    }

    @Test
    @DisplayName("Invalid call with spied service without delegating to real method")
    void testInvalidHelloEndpoint() {
        given()
                .when().get("/hello/invalid")
                .then()
                .statusCode(400) // spy is causing validation to malfunction
                .body(is("{code=must not be null, description=must not be blank}"));
    }

}