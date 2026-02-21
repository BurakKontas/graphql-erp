package tr.kontas.erp.crm.platform.persistence.activity;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.activity.*;

public class ActivityMapper {

    public static ActivityJpaEntity toEntity(Activity a) {
        ActivityJpaEntity e = new ActivityJpaEntity();
        e.setId(a.getId().asUUID());
        e.setTenantId(a.getTenantId().asUUID());
        e.setCompanyId(a.getCompanyId().asUUID());
        e.setActivityType(a.getActivityType().name());
        e.setSubject(a.getSubject());
        e.setOwnerId(a.getOwnerId());
        e.setStatus(a.getStatus().name());
        e.setRelatedEntityType(a.getRelatedEntityType());
        e.setRelatedEntityId(a.getRelatedEntityId());
        e.setScheduledAt(a.getScheduledAt());
        e.setCompletedAt(a.getCompletedAt());
        e.setDurationMinutes(a.getDurationMinutes());
        e.setDescription(a.getDescription());
        e.setOutcome(a.getOutcome());
        e.setFollowUpType(a.getFollowUpType());
        e.setFollowUpScheduledAt(a.getFollowUpScheduledAt());
        e.setFollowUpNote(a.getFollowUpNote());
        return e;
    }

    public static Activity toDomain(ActivityJpaEntity e) {
        return new Activity(
                ActivityId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                ActivityType.valueOf(e.getActivityType()),
                e.getSubject(), e.getOwnerId(),
                ActivityStatus.valueOf(e.getStatus()),
                e.getRelatedEntityType(), e.getRelatedEntityId(),
                e.getScheduledAt(), e.getCompletedAt(), e.getDurationMinutes(),
                e.getDescription(), e.getOutcome(),
                e.getFollowUpType(), e.getFollowUpScheduledAt(), e.getFollowUpNote()
        );
    }
}

