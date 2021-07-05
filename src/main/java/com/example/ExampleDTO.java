package com.example;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ExampleDTO {

    @NotNull
    private Long code;

    @NotBlank
    private String description;

    private String optional;

    public ExampleDTO() {
        // Jackson Requirement
    }

    public ExampleDTO(Long code, String description, String optional) {
        this.code = code;
        this.description = description;
        this.optional = optional;
    }

    @Override
    public String toString() {
        return "ExampleDTO{" +
                "code=" + code +
                ", description='" + description + '\'' +
                ", optional='" + optional + '\'' +
                '}';
    }
}
