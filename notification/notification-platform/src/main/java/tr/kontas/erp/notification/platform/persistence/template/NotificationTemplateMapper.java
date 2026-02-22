package tr.kontas.erp.notification.platform.persistence.template;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.NotificationChannel;
import tr.kontas.erp.notification.domain.template.NotificationTemplate;
import tr.kontas.erp.notification.domain.template.NotificationTemplateId;

public class NotificationTemplateMapper {

    public static NotificationTemplateJpaEntity toEntity(NotificationTemplate domain) {
        NotificationTemplateJpaEntity entity = new NotificationTemplateJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setNotificationKey(domain.getNotificationKey());
        entity.setChannel(domain.getChannel().name());
        entity.setLocale(domain.getLocale());
        entity.setSubject(domain.getSubject());
        entity.setBodyTemplate(domain.getBodyTemplate());
        entity.setSystemTemplate(domain.isSystemTemplate());
        entity.setActive(domain.isActive());
        return entity;
    }

    public static NotificationTemplate toDomain(NotificationTemplateJpaEntity entity) {
        return new NotificationTemplate(
                NotificationTemplateId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                entity.getNotificationKey(),
                NotificationChannel.valueOf(entity.getChannel()),
                entity.getLocale(),
                entity.getSubject(),
                entity.getBodyTemplate(),
                entity.isSystemTemplate(),
                entity.isActive()
        );
    }
}

