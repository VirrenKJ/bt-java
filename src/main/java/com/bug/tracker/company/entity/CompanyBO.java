package com.bug.tracker.company.entity;

import com.bug.tracker.common.object.Audit;
import com.bug.tracker.user.entity.UserBO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "company")
public class CompanyBO extends Audit {

  private static final long serialVersionUID = 5552166560392622180L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
  private CompanyDbDetailBO companyDbDetail;

  @ManyToMany
  @JsonBackReference
  @JoinTable(name = "company_employee", joinColumns = {@JoinColumn(name = "company_id", updatable = false, insertable = false)},
          inverseJoinColumns = {@JoinColumn(name = "user_id", updatable = false, insertable = false)})
  private List<UserBO> users = new ArrayList<>();

}
