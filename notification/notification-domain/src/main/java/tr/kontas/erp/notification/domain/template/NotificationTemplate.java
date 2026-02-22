package tr.kontas.erp.notification.domain.template;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.NotificationChannel;

@Getter
public class NotificationTemplate extends AggregateRoot<NotificationTemplateId> {

    private final TenantId tenantId;
    private String notificationKey;
    private NotificationChannel channel;
    private String locale;
    private String subject;
    private String bodyTemplate;
    private boolean systemTemplate;
    private boolean active;

    public NotificationTemplate(
            NotificationTemplateId id,
            TenantId tenantId,
            String notificationKey,
            NotificationChannel channel,
            String locale,
            String subject,
            String bodyTemplate,
            boolean systemTemplate,
            boolean active) {
        super(id);
        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (notificationKey == null || notificationKey.isBlank()) throw new IllegalArgumentException("notificationKey cannot be blank");
        if (channel == null) throw new IllegalArgumentException("channel cannot be null");
        if (bodyTemplate == null || bodyTemplate.isBlank()) throw new IllegalArgumentException("bodyTemplate cannot be blank");
        this.tenantId = tenantId;
        this.notificationKey = notificationKey;
        this.channel = channel;
        this.locale = locale != null ? locale : "TR";
        this.subject = subject;
        this.bodyTemplate = bodyTemplate;
        this.systemTemplate = systemTemplate;
        this.active = active;
    }

    public void updateTemplate(String subject, String bodyTemplate) {
        if (bodyTemplate == null || bodyTemplate.isBlank()) throw new IllegalArgumentException("bodyTemplate cannot be blank");
        this.subject = subject;
        this.bodyTemplate = bodyTemplate;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }
}

