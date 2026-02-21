package tr.kontas.erp.crm.platform.persistence.activity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "crm_activities")
@Getter
@Setter
@NoArgsConstructor
public class ActivityJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "activity_type", nullable = false)
    private String activityType;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "related_entity_type")
    private String relatedEntityType;

    @Column(name = "related_entity_id")
    private String relatedEntityId;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @Column(name = "description")
    private String description;

    @Column(name = "outcome")
    private String outcome;

    @Column(name = "follow_up_type")
    private String followUpType;

    @Column(name = "follow_up_scheduled_at")
    private LocalDateTime followUpScheduledAt;

    @Column(name = "follow_up_note")
    private String followUpNote;
}

