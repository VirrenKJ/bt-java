package com.bug.tracker.systemProfile.entity;

import com.bug.tracker.common.object.Audit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "project")
public class SystemProfileBO extends Audit {

  private static final long serialVersionUID = -8840236320072670666L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "platform")
  private String platform;

  @Column(name = "os_name")
  private String osName;

  @Column(name = "os_version")
  private String osVersion;

  @Column(name = "description")
  private String description;

  @Column(name = "delete_flag")
  private boolean deleteFlag;
}
