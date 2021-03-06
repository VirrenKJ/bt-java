package com.bug.tracker.config.tenantConfig;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

  private String defaultTenant = "default";

  @Override
  public String resolveCurrentTenantIdentifier() {
    String tenantId = TenantContext.getCurrentTenant();
    if (tenantId != null && !tenantId.isEmpty()) {
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
