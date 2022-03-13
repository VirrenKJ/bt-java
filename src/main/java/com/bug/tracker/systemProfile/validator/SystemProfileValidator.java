package com.bug.tracker.systemProfile.validator;

import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.common.object.APP_MSG;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.systemProfile.controller.SystemProfileController;
import com.bug.tracker.systemProfile.dto.SystemProfileTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = SystemProfileController.class)
public class SystemProfileValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    boolean support = GlobalCategoryTO.class.equals(aClass);
    if (!support) {
      support = PaginationCriteria.class.equals(aClass);
    }
    return support;
  }

  @Override
  public void validate(Object o, Errors errors) {
    SystemProfileTO systemProfileTO = (SystemProfileTO) o;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "platform", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "osName", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "osVersion", "BT001E", APP_MSG.RESPONSE.get("BT001E"));

  }
}
