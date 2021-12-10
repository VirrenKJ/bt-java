package com.bug.tracker.user.validator;

import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.user.controller.RoleController;
import com.bug.tracker.user.dto.RoleTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = RoleController.class)
public class RoleValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        boolean support = RoleTO.class.equals(aClass);
        if (!support) {
            support = SearchCriteriaObj.class.equals(aClass);
        }
        return support;
    }

    @Override
    public void validate(Object o, Errors errors) {

        RoleTO roleTO = (RoleTO) o;

//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "BT001E", APP_MSG.MESSAGE.get("BT001E"));
    }
}
