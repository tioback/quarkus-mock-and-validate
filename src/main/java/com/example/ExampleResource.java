package com.example;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class ExampleResource {

    @Inject
    ExampleService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/valid")
    public String validHello() {
        return service.hello(new ExampleDTO(1L, "a description", "something"));
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/invalid")
    public String invalidHello() {
        return service.hello(new ExampleDTO());
    }
}