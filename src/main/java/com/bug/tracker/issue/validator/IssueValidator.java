package com.bug.tracker.issue.validator;

import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.issue.controller.IssueController;
import com.bug.tracker.issue.dto.IssueTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = IssueController.class)
public class IssueValidator implements Validator {

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
    IssueTO issueTO = (IssueTO) o;

//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "BT001E", APP_MSG.MESSAGE.get("BT001E"));
  }
}
