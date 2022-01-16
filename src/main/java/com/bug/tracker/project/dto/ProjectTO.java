package com.bug.tracker.project.dto;

import com.bug.tracker.common.object.AuditTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectTO extends AuditTO {

  private Integer id;
  private String name;
  private String status;
  private boolean inheritCategoryFlag;
  private String viewStatus;
  private String description;
  private boolean deleteFlag;
}
