package com.bug.tracker.category.validator;

import com.bug.tracker.category.controller.GlobalCategoryController;
import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = GlobalCategoryController.class)
public class GlobalCategoryValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        boolean support = GlobalCategoryTO.class.equals(clazz);
        if (!support) {
            support = SearchCriteriaObj.class.equals(clazz);
        }
        return support;
    }

    @Override
    public void validate(Object target, Errors errors) {
        GlobalCategoryTO globalCategoryTO = (GlobalCategoryTO) target;

//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "BT001E", ApplicationMessages.MESSAGE.get("BT001E"));
    }

}
