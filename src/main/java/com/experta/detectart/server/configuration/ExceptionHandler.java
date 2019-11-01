package com.experta.detectart.server.configuration;

import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleConflict(final RuntimeException ex, final WebRequest request) throws JsonProcessingException {

      ObjectMapper mapper = new ObjectMapper();
      ObjectNode body = mapper.createObjectNode();
      body.put("timestamp", Instant.now().toString());
      body.put("status", HttpStatus.CONFLICT.value());
      body.put("error", HttpStatus.CONFLICT.getReasonPhrase());
      body.put("message", "There already is a Contact with this phone number assigned to the current User.");
      body.put("path", request.getDescription(false).substring(4));

      HttpHeaders headers =  new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      return handleExceptionInternal(ex
                                     , mapper.writeValueAsString(body)
                                     , headers
                                     , HttpStatus.CONFLICT
                                     , request);
    }
}

