package com.bug.tracker.company.dto;

import com.bug.tracker.user.entity.UserBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
  private CompanyDbDetailTO companyDbDetail;
  private List<UserBO> users;
}
