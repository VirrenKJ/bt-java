package com.bug.tracker.common.object;

import org.springframework.validation.Errors;

public class ValidationErrorBuilder {

    public static ValidationError fromBindingErrors(Errors errors) {

        ValidationError error = new ValidationError();
        Integer failObj=0;
        error.setStatus(400);
        error.setCode("BT007");
        // error.setMessage(errors.getFieldError().getDefaultMessage());
        error.setMessage(APP_MSG.MESSAGE.get("BT007"));

        for (org.springframework.validation.FieldError fieldError1 : errors.getFieldErrors()) {
            failObj++;
            FieldError fieldError = new FieldError();
            fieldError.setFieldName(fieldError1.getField());
            fieldError.setMessage(fieldError1.getDefaultMessage());
            error.getFieldErrors().add(fieldError);
        }
        error.setTotalErrorCount(failObj);
        return error;
    }
}
