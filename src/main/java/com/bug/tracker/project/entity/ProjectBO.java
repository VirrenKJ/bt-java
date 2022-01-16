package com.bug.tracker.project.entity;

import com.bug.tracker.common.object.Audit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "project")
public class ProjectBO extends Audit {

  private static final long serialVersionUID = 5360981864706583837L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "status")
  private String status;

  @Column(name = "inherit_category_flag")
  private boolean inheritCategoryFlag;

  @Column(name = "view_status")
  private String viewStatus;

  @Column(name = "description")
  private String description;

  @Column(name = "delete_flag")
  private boolean deleteFlag;
}
