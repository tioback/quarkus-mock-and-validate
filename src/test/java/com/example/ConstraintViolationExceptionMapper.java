package com.example;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException exception) {
		return Response.status(Response.Status.BAD_REQUEST).entity(exception.getConstraintViolations().stream()
				.collect(Collectors.toMap(this::extractField, ConstraintViolation::getMessage))).build();
	}

	private String extractField(ConstraintViolation<?> violation) {
		return StreamSupport.stream(violation.getPropertyPath().spliterator(), false).reduce((anterior, proximo) -> proximo)
				.map(Path.Node::getName).orElse(null);
	}

}
