package com.bug.tracker.issue.dto;

import com.bug.tracker.common.object.AuditTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueTO extends AuditTO {

  private Integer id;
  private Integer projectId;
  private String projectName;
  private Integer categoryId;
  private String categoryName;
  private String reproducibility;
  private String severity;
  private String priority;
  private Integer profileId;
  private String profileName;
  private Integer assignedId;
  private String assignedFirstName;
  private String assignedUsername;
  private Integer reportedById;
  private String reportedByFirstName;
  private String reportedByUsername;
  private String summary;
  private String description;
  private String stepsToReproduce;
  private String addInfo;
  private Integer documentId;
  private String viewStatus;
  private boolean deleteFlag;
}
