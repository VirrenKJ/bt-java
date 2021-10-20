package com.bug.tracker.config.tenantConfig;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

  private String defaultTenant = "bug_tracker";

  @Override
  public String resolveCurrentTenantIdentifier() {
    String tenantId = TenantContext.getCurrentTenant();
    if (tenantId != null) {
      return tenantId;
    } else {
      return defaultTenant;
    }
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }
}
