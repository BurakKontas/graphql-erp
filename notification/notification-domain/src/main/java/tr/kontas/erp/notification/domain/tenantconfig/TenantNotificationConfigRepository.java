package tr.kontas.erp.notification.domain.tenantconfig;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Optional;

public interface TenantNotificationConfigRepository {
    void save(TenantNotificationConfig config);
    Optional<TenantNotificationConfig> findByTenantId(TenantId tenantId);
    Optional<TenantNotificationConfig> findById(TenantNotificationConfigId id, TenantId tenantId);
}

