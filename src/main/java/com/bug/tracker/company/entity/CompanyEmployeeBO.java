package com.bug.tracker.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;

@Setter
@Getter
@Table(name = "company_employee")
public class CompanyEmployeeBO {

  private Integer id;
  private Integer companyId;
  private Integer userId;
}
