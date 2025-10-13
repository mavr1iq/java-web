package org.java.web.intergalacticmarketplace.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

import static org.springframework.http.ProblemDetail.forStatusAndDetail;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    FieldError fieldError = ex.getBindingResult().getFieldError();

    String errorMessage =
        "Validation failed for object: "
            + fieldError.getObjectName()
            + ". Field: "
            + fieldError.getField()
            + " - "
            + fieldError.getDefaultMessage();

    ProblemDetail problemDetail = forStatusAndDetail(BAD_REQUEST, errorMessage);
    problemDetail.setType(URI.create(request.getContextPath()));
    problemDetail.setTitle("Validation Error");

    return ResponseEntity.status(BAD_REQUEST).body(problemDetail);
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleProductNotFound(
      ProductNotFoundException ex, HttpServletRequest request) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problemDetail.setType(URI.create(request.getContextPath()));
    problemDetail.setTitle("Product Not Found");

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
  }
}
