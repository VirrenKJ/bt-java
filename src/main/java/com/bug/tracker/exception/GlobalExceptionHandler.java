package com.bug.tracker.exception;

import com.bug.tracker.common.object.APP_MSG;
import com.bug.tracker.common.object.FieldValidError;
import com.bug.tracker.common.object.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler({NullPointerException.class, EmptyResultDataAccessException.class, NoResultException.class})
  public ResponseEntity<ErrorMessage> handleNullPointerException(HttpServletRequest request, Exception ex) {
    ex.printStackTrace();
    ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), "GEX002",
            APP_MSG.RESPONSE.get("GEX002"), APP_MSG.HELP);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  @ExceptionHandler(SQLException.class)
  public ResponseEntity<ErrorMessage> handleSQLException(HttpServletRequest request, Exception ex) {
    ex.printStackTrace();
    ErrorMessage message = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GEX001",
            APP_MSG.RESPONSE.get("GEX001"), APP_MSG.HELP);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  @ExceptionHandler(ArithmeticException.class)
  public ResponseEntity<ValidationError> handleArithmeticException(HttpServletRequest request, Exception ex) {
    logger.info("ArithmeticException Occurred:: URL=" + request.getRequestURL());
    ValidationError error = new ValidationError();
    error.setStatus(400);
    error.setCode("GOL007");
    error.setMessage(ex.getMessage());
    FieldValidError fieldValidError = new FieldValidError();
    fieldValidError.setFieldName("AMOUNT");
    fieldValidError.setMessage(ex.getMessage());
    error.getFieldValidErrors().add(fieldValidError);
    return new ResponseEntity<>(error, HttpStatus.OK);
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<ErrorMessage> handleIOException(HttpServletRequest request, Exception ex) {
    logger.error("IOException handler executed *************************************** " + ex.getMessage());

    ErrorMessage message = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GEX002",
            ex.getMessage(), APP_MSG.HELP);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorMessage> handleDuplicateRecords(HttpServletRequest request, Exception ex) {
    logger.error("ConstraintViolationException handler executed ******************************************** :: " + ex.getMessage());

    ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "GEX003",
            APP_MSG.RESPONSE.get("GEX003"), APP_MSG.HELP);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleUserNotFoundException(HttpServletRequest request, Exception ex) {
    logger.error("UserNotFoundException handler executed ******************************************** :: " + ex.getMessage());

    ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(), "GEX002",
            ex.getMessage(), APP_MSG.HELP);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }
}
