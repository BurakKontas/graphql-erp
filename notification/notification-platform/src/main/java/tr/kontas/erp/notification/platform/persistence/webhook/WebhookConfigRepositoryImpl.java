package tr.kontas.erp.notification.platform.persistence.webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.WebhookStatus;
import tr.kontas.erp.notification.domain.webhook.WebhookConfig;
import tr.kontas.erp.notification.domain.webhook.WebhookConfigId;
import tr.kontas.erp.notification.domain.webhook.WebhookConfigRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WebhookConfigRepositoryImpl implements WebhookConfigRepository {

    private final JpaWebhookConfigRepository jpaRepository;

    @Override
    public void save(WebhookConfig config) {
        jpaRepository.save(WebhookConfigMapper.toEntity(config));
    }

    @Override
    public Optional<WebhookConfig> findById(WebhookConfigId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(WebhookConfigMapper::toDomain);
    }

    @Override
    public List<WebhookConfig> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(WebhookConfigMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<WebhookConfig> findByIds(List<WebhookConfigId> ids) {
        List<UUID> uuids = ids.stream().map(WebhookConfigId::asUUID).collect(Collectors.toList());
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(WebhookConfigMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<WebhookConfig> findActiveByEventType(String eventType, TenantId tenantId) {
        return jpaRepository.findByTenantIdAndStatus(tenantId.asUUID(), WebhookStatus.ACTIVE.name())
                .stream()
                .map(WebhookConfigMapper::toDomain)
                .filter(wh -> wh.getEventTypes().contains(eventType))
                .collect(Collectors.toList());
    }
}

