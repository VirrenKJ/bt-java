package com.bug.tracker.common.object;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AuditListenerHandler {

    @PrePersist
    public void onPrePersist(Audit audit) {
        if(audit != null) {
            audit.setCreatedBy(2502L);
            audit.setUpdatedBy(2502L);
        }
    }

    @PreUpdate
    public void onPreUpdate(Audit audit) {
        if(audit != null) {
            audit.setUpdatedBy(2502L);
        }
    }
}
