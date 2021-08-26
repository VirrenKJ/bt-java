package com.bug.tracker.category.dto;

import com.bug.tracker.common.object.AuditTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalCategoryTO extends AuditTO {

    private Integer id;
    private String name;
    private Integer assignedId;

}
