package com.bug.tracker.common.service;

import java.util.HashMap;
import java.util.Map;

public class APP_MSG {

    private APP_MSG() {
        // private constructor
    }

    public static final Map<String, String> MESSAGE = new HashMap<>();

    static {
        MESSAGE.put("BT001", "Created successfully!");
        MESSAGE.put("BT002", "Updated successfully!");
        MESSAGE.put("BT003", "Fetched List!");
        MESSAGE.put("BT004", "Selected section!");
        MESSAGE.put("BT005", "Deleted successfully!");
        MESSAGE.put("BT006", "Empty List!");
        MESSAGE.put("BT007", "Invalid argument!");
        MESSAGE.put("BT001E", "Required Field!");
    }

    public static String getMessage(String moduleName, String fieldName, String messageCode) {
        String messageStr = MESSAGE.get(messageCode);
        String returnMessage = "";
        if (messageStr == null) {
            messageStr = "Invalid Message Code";
        }
        if (moduleName != null && !moduleName.trim().isEmpty()) {
            returnMessage = moduleName.trim() + " ";
        }
        if (fieldName != null && !fieldName.trim().isEmpty()) {
            returnMessage += fieldName.trim() + " ";
        }
        returnMessage += messageStr;
        return returnMessage;
    }
}
