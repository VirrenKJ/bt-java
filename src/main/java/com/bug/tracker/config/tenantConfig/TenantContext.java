package com.bug.tracker.config.tenantConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContext {

  private static final Logger logger = LoggerFactory.getLogger(TenantContext.class.getName());

  private static final ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

  public static String getCurrentTenant() {
    return currentTenant.get();
  }

  public static void setCurrentTenant(String tenant) {
    logger.debug("Setting tenant to ======================================> " + tenant);
    currentTenant.set(tenant);
  }

  public static void clear() {
    currentTenant.remove();
  }
}
