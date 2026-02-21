package tr.kontas.erp.app.crm.dtos;

public record ActivityPayload(
        String id,
        String companyId,
        String activityType,
        String subject,
        String ownerId,
        String status,
        String relatedEntityType,
        String relatedEntityId,
        String scheduledAt,
        String completedAt,
        int durationMinutes,
        String description,
        String outcome,
        String followUpType,
        String followUpScheduledAt,
        String followUpNote
) {}

