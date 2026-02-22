package tr.kontas.erp.notification.domain.webhook;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface WebhookConfigRepository {
    void save(WebhookConfig config);
    Optional<WebhookConfig> findById(WebhookConfigId id, TenantId tenantId);
    List<WebhookConfig> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<WebhookConfig> findByIds(List<WebhookConfigId> ids);
    List<WebhookConfig> findActiveByEventType(String eventType, TenantId tenantId);
}

