package com.bug.tracker.config;

import com.bug.tracker.user.dto.RoleTO;
import com.bug.tracker.user.dto.UserTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserSessionContext {

  private static final Logger logger = LoggerFactory.getLogger(UserSessionContext.class.getName());

  private static final ThreadLocal<UserTO> currentTenant = new ThreadLocal<>();

  public static void setCurrentTenant(UserTO userTO) {
    logger.debug("Setting User session Context ==================================================> " + userTO);
    currentTenant.set(userTO);
  }

  public static UserTO getCurrentTenant() {
    return currentTenant.get();
  }

  public static RoleTO getUserRole() {
    UserTO user = UserSessionContext.getCurrentTenant();
    if(user != null && user.getRoles() != null && user.getRoles().size() > 0) {
      return user.getRoles().get(0);
    }
    return null;
  }

  public static void clear() {
    currentTenant.set(null);
  }

}
