package com.bug.tracker.user.validator;

import com.bug.tracker.common.object.APP_MSG;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.user.controller.RoleController;
import com.bug.tracker.user.dto.RoleTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = RoleController.class)
public class RoleValidator implements Validator {
  @Override
  public boolean supports(Class<?> aClass) {
    boolean support = RoleTO.class.equals(aClass);
    if (!support) {
      support = PaginationCriteria.class.equals(aClass);
    }
    return support;
  }

  @Override
  public void validate(Object o, Errors errors) {
    RoleTO roleTO = (RoleTO) o;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roleName", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
  }
}
