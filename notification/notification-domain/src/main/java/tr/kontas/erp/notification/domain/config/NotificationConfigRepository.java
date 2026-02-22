package tr.kontas.erp.notification.domain.config;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface NotificationConfigRepository {
    void save(NotificationConfig config);
    Optional<NotificationConfig> findById(NotificationConfigId id, TenantId tenantId);
    Optional<NotificationConfig> findByEventTypeAndCompany(String eventType, TenantId tenantId, CompanyId companyId);
    List<NotificationConfig> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<NotificationConfig> findByIds(List<NotificationConfigId> ids);
}

