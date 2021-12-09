package com.bug.tracker.company.entity;

import com.bug.tracker.common.object.Audit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "company_db_detail")
public class CompanyDbDetailCustomBO extends Audit {

  private static final long serialVersionUID = -6902934573708847383L;

  @Id
  @Basic(optional = false)
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

  @Column(name = "db_url")
  private String dbUrl;

  @Column(name = "db_username")
  private String dbUsername;

  @Column(name = "db_password")
  private String dbPassword;

  @Column(name = "delete_flag")
  private boolean deleteFlag;
}
