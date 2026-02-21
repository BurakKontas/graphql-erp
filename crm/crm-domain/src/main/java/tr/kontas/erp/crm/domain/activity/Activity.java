package tr.kontas.erp.crm.domain.activity;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.event.ActivityCreatedEvent;

import java.time.LocalDateTime;

@Getter
public class Activity extends AggregateRoot<ActivityId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private ActivityType activityType;
    private String subject;
    private String ownerId;
    private ActivityStatus status;
    private String relatedEntityType;
    private String relatedEntityId;
    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;
    private int durationMinutes;
    private String description;
    private String outcome;
    private String followUpType;
    private LocalDateTime followUpScheduledAt;
    private String followUpNote;

    public Activity(ActivityId id, TenantId tenantId, CompanyId companyId,
                    ActivityType activityType, String subject, String ownerId,
                    ActivityStatus status, String relatedEntityType, String relatedEntityId,
                    LocalDateTime scheduledAt, LocalDateTime completedAt, int durationMinutes,
                    String description, String outcome, String followUpType,
                    LocalDateTime followUpScheduledAt, String followUpNote) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.activityType = activityType;
        this.subject = subject;
        this.ownerId = ownerId;
        this.status = status;
        this.relatedEntityType = relatedEntityType;
        this.relatedEntityId = relatedEntityId;
        this.scheduledAt = scheduledAt;
        this.completedAt = completedAt;
        this.durationMinutes = durationMinutes;
        this.description = description;
        this.outcome = outcome;
        this.followUpType = followUpType;
        this.followUpScheduledAt = followUpScheduledAt;
        this.followUpNote = followUpNote;
    }


    public static Activity create(ActivityId id, TenantId tenantId, CompanyId companyId,
                                  ActivityType type, String subject, String ownerId,
                                  String relatedEntityType, String relatedEntityId,
                                  LocalDateTime scheduledAt, String description) {
        Activity a = new Activity(id, tenantId, companyId, type, subject, ownerId,
                ActivityStatus.PLANNED, relatedEntityType, relatedEntityId,
                scheduledAt, null, 0, description, null, null, null, null);
        a.registerEvent(new ActivityCreatedEvent(
                id.asUUID(), tenantId.asUUID(), companyId.asUUID(),
                type.name(), subject, relatedEntityType, relatedEntityId));
        return a;
    }


    public void complete(String outcome, int durationMinutes,
                         String followUpType, LocalDateTime followUpScheduledAt, String followUpNote) {
        if (status != ActivityStatus.PLANNED) {
            throw new IllegalStateException("Can only complete PLANNED activities");
        }
        this.status = ActivityStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.outcome = outcome;
        this.durationMinutes = durationMinutes;
        this.followUpType = followUpType;
        this.followUpScheduledAt = followUpScheduledAt;
        this.followUpNote = followUpNote;
    }


    public void cancel() {
        if (status != ActivityStatus.PLANNED) {
            throw new IllegalStateException("Can only cancel PLANNED activities");
        }
        this.status = ActivityStatus.CANCELLED;
    }
}

