package com.bug.tracker.company.entity;

import com.bug.tracker.common.object.Audit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "company")
public class CompanyCustomBO extends Audit {

  private static final long serialVersionUID = -5924658335416805693L;

  @Id
  @Basic(optional = false)
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

  @Column(name = "company_db_detail_id", insertable = false, updatable = false)
  private Integer companyDbDetailId;

  @Column(name = "user_id")
  private Integer userId;

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

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "company_db_detail_id")
  private CompanyDbDetailCustomBO companyDbDetail;

}
