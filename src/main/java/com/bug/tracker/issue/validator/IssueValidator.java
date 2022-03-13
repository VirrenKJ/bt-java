package com.bug.tracker.issue.validator;

import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.common.object.APP_MSG;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.issue.controller.IssueController;
import com.bug.tracker.issue.dto.IssueTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
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

    if (issueTO.getProjectId() == null) {
      errors.rejectValue("projectId", "PMS001E", APP_MSG.RESPONSE.get("BT001E"));
    }
    if (issueTO.getCategoryId() == null) {
      errors.rejectValue("categoryId", "PMS001E", APP_MSG.RESPONSE.get("BT001E"));
    }
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reproducibility", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "severity", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "priority", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    if (issueTO.getProfileId() == null) {
      errors.rejectValue("profileId", "PMS001E", APP_MSG.RESPONSE.get("BT001E"));
    }
    if (issueTO.getAssignedId() == null) {
      errors.rejectValue("assignedId", "PMS001E", APP_MSG.RESPONSE.get("BT001E"));
    }
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "summary", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
  }
}
