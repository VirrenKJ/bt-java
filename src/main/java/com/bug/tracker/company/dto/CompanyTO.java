package com.bug.tracker.company.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyTO {

  private Integer id;
  private Integer companyDbDetailId;
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
  CompanyDbDetailTO companyDbDetail;
}
