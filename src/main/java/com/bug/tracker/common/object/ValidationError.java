package com.bug.tracker.common.object;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ValidationError {

    private int status;
    private String code;
    private String message;
    private Integer totalErrorCount;
    private List<FieldError> fieldErrors = new ArrayList<>();

    public ValidationError() {
    }

    public ValidationError(int status, String errorCode, String errorMessage) {
        super();
        this.status = status;
        this.code = errorCode;
        this.message = errorMessage;
    }


    @Override
    public String toString() {
        return "ValidationError{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", failObj=" + totalErrorCount +
                ", fieldErrors=" + fieldErrors +
                '}';
    }
}
