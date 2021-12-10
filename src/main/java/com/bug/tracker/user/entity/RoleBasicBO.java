package com.bug.tracker.user.entity;

import com.bug.tracker.common.object.Audit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "role")
public class RoleBasicBO extends Audit {

  private static final long serialVersionUID = -2002042439964715252L;

  @Id
  @Basic(optional = false)
  @Column(name = "role_id", unique = true, nullable = false)
  private Integer roleId;

  @Column(name = "role_name")
  private String roleName;

  @Column(name = "description")
  private String description;

  @Column(name = "delete_flag")
  private boolean deleteFlag;

}
