package com.bug.tracker.company.entity;

import com.bug.tracker.common.object.Audit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "company")
public class CompanyDetailsNewTenantBO extends Audit {

  private static final long serialVersionUID = 4071974405013814138L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "db_name")
  private String dbName;

  @Column(name = "db_uuid")
  private String dbUuid;

  @Column(name = "email")
  private String email;

  @Column(name = "contact_number")
  private Long contactNumber;

  @Column(name = "industry_type")
  private String industryType;

  @Column(name = "pin_code")
  private int pinCode;

  @Column(name = "state")
  private String state;

  @Column(name = "city")
  private String city;

  @Column(name = "delete_flag")
  private boolean deleteFlag;
}