package com.bug.tracker.common.object;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuditTO {

    private Date createdAt;
    private Long createdBy;
    private Date updatedAt;
    private Long updatedBy;

}
