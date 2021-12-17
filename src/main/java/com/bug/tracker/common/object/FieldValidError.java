package com.bug.tracker.common.object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldValidError {
    private String fieldName;
    private String message;
}
