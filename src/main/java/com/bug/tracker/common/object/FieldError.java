package com.bug.tracker.common.object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldError {
    private String fieldName;
    private String message;
}
