package io.maeda.apps.bagual.controllers;

import io.maeda.apps.bagual.dtos.ResponsePayload;
import io.maeda.apps.bagual.exceptions.AliasException;
import io.maeda.apps.bagual.exceptions.ShortUrlNotFoundException;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { AliasException.class})
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { ShortUrlNotFoundException.class})
    protected ResponseEntity<Object> notFound(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> constraintViolation(ConstraintViolationException ex, WebRequest request) {

        Collection<ConstraintViolationsItem> violationsItems = ex.getConstraintViolations()
                .stream()
                .map(item -> new ConstraintViolationsItem(item.getMessage(), item.getInvalidValue().toString()))
                .collect(Collectors.toSet());


        ResponsePayload<Collection<ConstraintViolationsItem>> response = ResponsePayload
                .<Collection<ConstraintViolationsItem>>builder()
                .content(violationsItems)
                .code(Integer.toString(HttpStatus.FORBIDDEN.value()))
                .message("NOT_CREATED")
                .build();

        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @Value
    class ConstraintViolationsItem {
        private String violation;
        private String value;
    }

}
