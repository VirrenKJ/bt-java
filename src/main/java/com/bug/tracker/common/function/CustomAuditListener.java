package com.bug.tracker.common.function;

import com.bug.tracker.common.object.Audit;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class CustomAuditListener {

    @PrePersist
    public void onPrePersist(Audit audit){
        if (audit != null) {

        }
    }

    @PreUpdate
    public void onPreUpdate(Audit audit){
        if (audit != null) {
            
        }
    }

}
