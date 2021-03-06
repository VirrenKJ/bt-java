package com.bug.tracker.category.validator;

import com.bug.tracker.category.controller.GlobalCategoryController;
import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.common.object.APP_MSG;
import com.bug.tracker.common.object.PaginationCriteria;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = GlobalCategoryController.class)
public class GlobalCategoryValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    boolean support = GlobalCategoryTO.class.equals(aClass);
    if (!support) {
      support = PaginationCriteria.class.equals(aClass);
    }
    return support;
  }

  @Override
  public void validate(Object target, Errors errors) {
    GlobalCategoryTO globalCategoryTO = (GlobalCategoryTO) target;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    if (globalCategoryTO.getAssignedId() == null) {
      errors.rejectValue("assignedId", "PMS001E", APP_MSG.RESPONSE.get("BT001E"));
    }
  }

}
