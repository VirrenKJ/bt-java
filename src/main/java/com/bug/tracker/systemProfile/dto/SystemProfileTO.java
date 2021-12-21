package com.bug.tracker.systemProfile.dto;

import com.bug.tracker.common.object.AuditTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemProfileTO extends AuditTO {

  private Integer id;
  private String platform;
  private String osName;
  private String osVersion;
  private String description;
  private boolean deleteFlag;

}
