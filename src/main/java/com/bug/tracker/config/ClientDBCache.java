package com.bug.tracker.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientDBCache {

  private static final Logger logger = LoggerFactory.getLogger(ClientDBCache.class.getName());

  public static final Map<String, String> dbMap = new ConcurrentHashMap<>();

  public static Map<String, DriverManagerDataSource> driverManagerMap = new ConcurrentHashMap<>();

  static {
    dbMap.put("default", "bug_tracker");
  }

  public static String getKey(String key) {
    for (Map.Entry<String, String> entry : getAllKey().entrySet()) {
      logger.debug(entry.getKey() + "---------------------------------" + entry.getValue());
    }
    logger.debug("***********************" + dbMap.get(key));
    return dbMap.get(key);
  }

  public static Map<String, String> getAllKey() {
    return dbMap;
  }
}
