package com.bug.tracker.common.object;

import lombok.*;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ValidationError {

  private int status;
  private String code;
  private String message;
  private Integer totalErrorCount;
  private List<FieldValidError> fieldValidErrors = new ArrayList<>();

  public static ValidationError fromBindingErrors(Errors errors) {
    ValidationError error = new ValidationError();
    Integer failObj = 0;
    error.setStatus(400);
    error.setCode("CM001E");
    error.setMessage(APP_MSG.RESPONSE.get("CM001E"));

    for (org.springframework.validation.FieldError fieldError : errors.getFieldErrors()) {
      failObj++;
      FieldValidError fieldValidError = new FieldValidError();
      fieldValidError.setFieldName(fieldError.getField());
      fieldValidError.setMessage(fieldError.getDefaultMessage());
      error.getFieldValidErrors().add(fieldValidError);
    }
    error.setTotalErrorCount(failObj);
    return error;
  }

  public ValidationError(int status, String errorCode, String errorMessage) {
    this.status = status;
    this.code = errorCode;
    this.message = errorMessage;
  }
}
