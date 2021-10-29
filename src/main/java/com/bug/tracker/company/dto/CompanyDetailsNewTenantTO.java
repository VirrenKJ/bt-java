package com.bug.tracker.company.dto;

import com.bug.tracker.common.object.AuditTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDetailsNewTenantTO extends AuditTO {

  private Integer id;
  private String name;
  private String dbName;
  private String dbUuid;
  private String email;
  private Long contactNumber;
  private String industryType;
  private int pinCode;
  private String state;
  private String city;
  private boolean deleteFlag;
}
