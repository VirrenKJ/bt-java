package com.bug.tracker.user.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class UserAuthority implements GrantedAuthority {

  private static final long serialVersionUID = -3091574199895366733L;

  private String authority;

  public UserAuthority(String authority) {
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return this.authority;
  }
}
