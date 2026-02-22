package tr.kontas.erp.notification.domain.preference;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface NotificationPreferenceRepository {
    void save(NotificationPreference preference);
    Optional<NotificationPreference> findById(NotificationPreferenceId id, TenantId tenantId);
    Optional<NotificationPreference> findByUserAndKey(String userId, String notificationKey, TenantId tenantId);
    List<NotificationPreference> findByUserId(String userId, TenantId tenantId);
    List<NotificationPreference> findByIds(List<NotificationPreferenceId> ids);
}

