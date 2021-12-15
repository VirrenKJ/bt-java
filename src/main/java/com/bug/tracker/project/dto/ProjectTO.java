package com.bug.tracker.project.dto;

import com.bug.tracker.common.object.AuditTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectTO extends AuditTO {

  private Integer id;
  private String name;
  private Integer statusId;
  private boolean inheritCategoryFlag;
  private Integer viewStatusId;
  private String description;
  private boolean deleteFlag;
}
