package com.example;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;

@ApplicationScoped
public class ExampleService {

    public String hello(@Valid ExampleDTO example) {
        return "hello from service: " + example;
    }

}