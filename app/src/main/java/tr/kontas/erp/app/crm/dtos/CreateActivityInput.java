package tr.kontas.erp.app.crm.dtos;

public record CreateActivityInput(
        String companyId,
        String activityType,
        String subject,
        String ownerId,
        String relatedEntityType,
        String relatedEntityId,
        String scheduledAt,
        String description
) {}

