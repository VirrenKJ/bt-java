package com.bug.tracker.company.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompanyEmployeeBO {

  private Integer id;
  private Integer companyId;
  private Integer userId;
}
