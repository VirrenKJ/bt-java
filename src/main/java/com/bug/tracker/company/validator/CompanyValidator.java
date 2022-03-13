package com.bug.tracker.company.validator;

import com.bug.tracker.common.object.APP_MSG;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.company.dto.CompanyTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = CompanyValidator.class)
public class CompanyValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    boolean support = CompanyTO.class.equals(aClass);
    if (!support) {
      support = PaginationCriteria.class.equals(aClass);
    }
    return support;
  }

  @Override
  public void validate(Object o, Errors errors) {
    CompanyTO companyTO = (CompanyTO) o;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "industryType", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "state", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    if (companyTO.getContactNumber() == null) {
      errors.rejectValue("contactNumber", "PMS001E", APP_MSG.RESPONSE.get("BT001E"));
    }
  }
}
