package com.bug.tracker.master.dto;

import com.bug.tracker.common.object.APP_MSG;
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

    public static ResponseTO responseBuilder(int status, String code, String path, String key, Object data) {
        ResponseTO response = new ResponseTO();
        response.setStatus(status);
        response.setPath(path);
        response.setMessage(APP_MSG.RESPONSE.get(code));
        response.setCode(code);
        response.putData(key, data);
        return response;
    }

    public void putData(String key, Object Value) {
        data.put(key, Value);
    }
}
