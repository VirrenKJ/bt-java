package com.bug.tracker.company.validator;

import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.company.dto.CompanyTO;
import lombok.SneakyThrows;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = CompanyValidator.class)
public class CompanyValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    boolean support = CompanyTO.class.equals(aClass);
    if (!support) {
      support = SearchCriteriaObj.class.equals(aClass);
    }
    return support;
  }

  @Override
  public void validate(Object o, Errors errors) {

  }
}
