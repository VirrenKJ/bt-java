package com.bug.tracker.user.dto;

import com.bug.tracker.common.object.AuditTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleTO extends AuditTO {

    private Integer id;
    private String roleName;
    private boolean deleteFlag;
}
