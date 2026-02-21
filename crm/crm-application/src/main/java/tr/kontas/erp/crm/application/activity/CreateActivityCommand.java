package tr.kontas.erp.crm.application.activity;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.time.LocalDateTime;

public record CreateActivityCommand(
        CompanyId companyId,
        String activityType,
        String subject,
        String ownerId,
        String relatedEntityType,
        String relatedEntityId,
        LocalDateTime scheduledAt,
        String description
) {}

