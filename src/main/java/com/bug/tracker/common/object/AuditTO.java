package com.bug.tracker.common.object;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuditTO {

    private Date createdAt;
    private Integer createdBy;
    private Date updatedAt;
    private Integer updatedBy;

}
