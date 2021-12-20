package com.bug.tracker.issue.entity;

import com.bug.tracker.common.object.Audit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "project")
public class IssueBO extends Audit {

  private static final long serialVersionUID = -4125598754816033077L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "project_id")
  private Integer projectId;

  @Column(name = "category_id")
  private Integer categoryId;

  @Column(name = "reproducibility_id")
  private Integer reproducibilityId;

  @Column(name = "severity_id")
  private Integer severityId;

  @Column(name = "priority_id")
  private Integer priorityId;

  @Column(name = "profile_id")
  private Integer profileId;

  @Column(name = "assigned_id")
  private Integer assignedId;

  @Column(name = "summary")
  private String summary;

  @Column(name = "description")
  private String description;

  @Column(name = "steps_to_reproduce")
  private String stepsToReproduce;

  @Column(name = "add_info")
  private String addInfo;

  @Column(name = "document_id")
  private Integer documentId;

  @Column(name = "view_status")
  private String viewStatus;

  @Column(name = "delete_flag")
  private boolean deleteFlag;

}
