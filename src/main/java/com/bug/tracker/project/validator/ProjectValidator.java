package com.bug.tracker.project.validator;

import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.project.controller.ProjectController;
import com.bug.tracker.project.dto.ProjectTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = ProjectController.class)
public class ProjectValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    boolean support = GlobalCategoryTO.class.equals(aClass);
    if (!support) {
      support = SearchCriteriaObj.class.equals(aClass);
    }
    return support;
  }

  @Override
  public void validate(Object o, Errors errors) {
    ProjectTO projectTO = (ProjectTO) o;

//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "BT001E", APP_MSG.MESSAGE.get("BT001E"));
  }
}
