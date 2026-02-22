package tr.kontas.erp.notification.domain.template;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.NotificationChannel;

import java.util.List;
import java.util.Optional;

public interface NotificationTemplateRepository {
    void save(NotificationTemplate template);
    Optional<NotificationTemplate> findById(NotificationTemplateId id, TenantId tenantId);
    Optional<NotificationTemplate> findByKeyAndChannel(String notificationKey, NotificationChannel channel, TenantId tenantId);
    List<NotificationTemplate> findByTenantId(TenantId tenantId);
    List<NotificationTemplate> findByIds(List<NotificationTemplateId> ids);
}

