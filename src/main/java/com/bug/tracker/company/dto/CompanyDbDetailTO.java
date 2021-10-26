package com.bug.tracker.company.dto;

import com.bug.tracker.common.object.AuditTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDbDetailTO extends AuditTO {

  private Integer id;
  private String dbUrl;
  private String dbUsername;
  private String dbPassword;
  private boolean deleteFlag;
}
