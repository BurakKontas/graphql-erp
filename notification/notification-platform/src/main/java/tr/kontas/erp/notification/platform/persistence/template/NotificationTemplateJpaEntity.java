package tr.kontas.erp.notification.platform.persistence.template;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "notification_templates")
@Getter
@Setter
@NoArgsConstructor
public class NotificationTemplateJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "notification_key", nullable = false)
    private String notificationKey;

    @Column(name = "channel", nullable = false)
    private String channel;

    @Column(name = "locale", nullable = false)
    private String locale;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body_template", nullable = false, columnDefinition = "TEXT")
    private String bodyTemplate;

    @Column(name = "system_template", nullable = false)
    private boolean systemTemplate;

    @Column(name = "active", nullable = false)
    private boolean active;
}

