package com.bug.tracker.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetTO {

  private Integer userId;
  private String oldPassword;
  private String newPassword;
  private String confirmNewPassword;
}
