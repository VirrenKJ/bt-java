package com.bug.tracker.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordTO {
  private String token;
  private String newPassword;
  private String confirmNewPassword;
}
