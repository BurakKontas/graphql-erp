package tr.kontas.erp.notification.platform.persistence.webhook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaWebhookConfigRepository extends JpaRepository<WebhookConfigJpaEntity, UUID> {
    Optional<WebhookConfigJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<WebhookConfigJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<WebhookConfigJpaEntity> findByIdIn(List<UUID> ids);
    List<WebhookConfigJpaEntity> findByTenantIdAndStatus(UUID tenantId, String status);
}

