package com.bug.tracker.master.dto;

import com.bug.tracker.common.service.APP_MSG;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class ResponseTO {

    private int status;

    private String message;

    private String path;

    private String code;

    private Map<String, Object> data = new HashMap<>();

    private Map<String, String> errorList;

    private List<FieldError> fieldErrors;

    public static ResponseTO responseBuilder(int setStatus, String setPath, String setCode, Object data) {
        ResponseTO response = new ResponseTO();
        response.setStatus(setStatus);
        response.setPath("/" + setPath);
        response.setMessage(APP_MSG.getMessage(null, null, setCode));
        response.setCode(setCode);
        response.putData(setPath, data);
        return response;
    }

    public void putData(String key, Object Value) {
        data.put(key, Value);
    }
}
