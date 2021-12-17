package com.bug.tracker.common.object;

import java.util.HashMap;
import java.util.Map;

public class APP_MSG {

  private APP_MSG() {
    // private constructor
  }

  public static final Map<String, String> RESPONSE = new HashMap<>();

  static {
    RESPONSE.put("BT001", "Created successfully!");
    RESPONSE.put("BT002", "Updated successfully!");
    RESPONSE.put("BT003", "Fetched List!");
    RESPONSE.put("BT004", "Selected section!");
    RESPONSE.put("BT005", "Deleted successfully!");
    RESPONSE.put("BT006", "Empty!");
    RESPONSE.put("BT007", "Invalid argument!");
    RESPONSE.put("BT001E", "Required Field!");
    RESPONSE.put("BT002E", "Must be unique!");
  }
}
