package com.bug.tracker.user.dto;

import com.bug.tracker.common.object.AuditTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PasswordResetTokenTO extends AuditTO {

  private Long id;
  private Integer userId;
  private String token;
  private UserTO user;
  private Date expiryDate;
}
