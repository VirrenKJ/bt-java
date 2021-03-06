package com.bug.tracker.user.validator;

import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.APP_MSG;
import com.bug.tracker.user.controller.UserController;
import com.bug.tracker.user.dto.UserTO;
import com.bug.tracker.user.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = UserController.class)
public class UserValidator implements Validator {

  @Autowired
  private UserService userService;

  @Override
  public boolean supports(Class<?> aClass) {
    boolean support = UserTO.class.equals(aClass);
    if (!support) {
      support = PaginationCriteria.class.equals(aClass);
    }
    return support;
  }

  @SneakyThrows
  @Override
  public void validate(Object o, Errors errors) {
    UserTO userTO = (UserTO) o;

    if (userTO.getUsername() == null) {
      errors.rejectValue("username", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    } else {
      UserTO user = userService.getUserByUsername(userTO.getUsername());
      if (user != null && userTO.getId() == null) {
        errors.rejectValue("username", "BT002E", APP_MSG.RESPONSE.get("BT002E"));
      }
    }
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "BT001E", APP_MSG.RESPONSE.get("BT001E"));
  }
}
