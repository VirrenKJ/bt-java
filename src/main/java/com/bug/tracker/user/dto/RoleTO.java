package com.bug.tracker.user.dto;

import com.bug.tracker.common.object.AuditTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleTO extends AuditTO {

    private Integer roleId;
    private String roleName;
    private String description;
    private boolean deleteFlag;
}
