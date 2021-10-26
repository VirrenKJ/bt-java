package com.bug.tracker.common.object;

import com.bug.tracker.config.UserSessionContext;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AuditListenerHandler {

    @PrePersist
    public void onPrePersist(Audit audit) {
        if(audit != null) {
            audit.setCreatedBy(UserSessionContext.getCurrentTenant().getId());
            audit.setUpdatedBy(UserSessionContext.getCurrentTenant().getId());
        }
    }

    @PreUpdate
    public void onPreUpdate(Audit audit) {
        if(audit != null) {
            audit.setUpdatedBy(UserSessionContext.getCurrentTenant().getId());
        }
    }
}
