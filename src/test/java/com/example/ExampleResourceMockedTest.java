package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class ExampleResourceMockedTest {

    @InjectMock
    ExampleService service;

    @Test
    @DisplayName("Valid call with mocked service and delegating to real method")
    void testValidHelloEndpointDelegated() {
        when(service.hello(any())).thenCallRealMethod();

        given()
                .when().get("/hello/valid")
                .then()
                .statusCode(200)
                .body(is("hello from service: ExampleDTO{code=1, description='a description', optional='something'}"));
    }

    @Test
    @DisplayName("Invalid call with mocked service and delegating to real method")
    void testInvalidHelloEndpointDelegated() {
        when(service.hello(any())).thenCallRealMethod();

        given()
                .when().get("/hello/invalid")
                .then()
                .statusCode(400) // mock is causing ExampleResource to responde with status 204
                .body(is("{code=must not be null, description=must not be blank}"));
    }

    @Test
    @DisplayName("Valid call with mocked service without delegating to real method")
    void testValidHelloEndpoint() {
        given()
                .when().get("/hello/valid")
                .then()
                .statusCode(200) // mock is causing ExampleResource to respond with status 204
                .body(is("hello from service: ExampleDTO{code=1, description='a description', optional='something'}"));
    }

    @Test
    @DisplayName("Invalid call with mocked service without delegating to real method")
    void testInvalidHelloEndpoint() {
        given()
                .when().get("/hello/invalid")
                .then()
                .statusCode(400) // mock is causing ExampleResource to respond with status 204
                .body(is("{code=must not be null, description=must not be blank}"));
    }

}