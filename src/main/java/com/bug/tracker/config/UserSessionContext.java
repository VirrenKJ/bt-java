package com.bug.tracker.config;

import com.bug.tracker.user.entity.UserBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserSessionContext {

  private static final Logger logger = LoggerFactory.getLogger(UserSessionContext.class.getName());

  private static final ThreadLocal<UserBO> currentTenant = new ThreadLocal<>();

  public static void setCurrentTenant(UserBO userBO) {
    logger.debug("Setting User session Context ==================================================> " + userBO);
    currentTenant.set(userBO);
  }

  public static UserBO getCurrentTenant() {
    return currentTenant.get();
  }

  public static Collection<? extends GrantedAuthority> getUserRole() {
    UserBO userBO = UserSessionContext.getCurrentTenant();
    if (userBO != null && userBO.getAuthorities() != null && userBO.getAuthorities().size() > 0) {
      return userBO.getAuthorities();
    }
    return null;
  }

  public static void clear() {
    currentTenant.set(null);
  }

}
