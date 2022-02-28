package com.bug.tracker.issue.dto;

import com.bug.tracker.common.object.AuditTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueTO extends AuditTO {

  private Integer id;
  private Integer projectId;
  private Integer categoryId;
  private String reproducibility;
  private String severity;
  private String priority;
  private Integer profileId;
  private Integer assignedId;
  private String summary;
  private String description;
  private String stepsToReproduce;
  private String addInfo;
  private Integer documentId;
  private String viewStatus;
  private boolean deleteFlag;
}
