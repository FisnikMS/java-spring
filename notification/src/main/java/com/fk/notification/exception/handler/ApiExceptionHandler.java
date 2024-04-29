package com.fk.notification.exception.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fk.notification.exception.EntityNotFoundException;


@ControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity handleEntityNotFoundException(EntityNotFoundException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("message", ex.getMessage());
    body.put("status", HttpStatus.NOT_FOUND.value());
    body.put("error", "Not found");
    body.put("timestamp", new Date());
    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

}
