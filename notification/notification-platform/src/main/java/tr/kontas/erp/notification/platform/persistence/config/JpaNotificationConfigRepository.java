package tr.kontas.erp.notification.platform.persistence.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaNotificationConfigRepository extends JpaRepository<NotificationConfigJpaEntity, UUID> {
    Optional<NotificationConfigJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<NotificationConfigJpaEntity> findByEventTypeAndTenantIdAndCompanyId(String eventType, UUID tenantId, UUID companyId);
    List<NotificationConfigJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<NotificationConfigJpaEntity> findByIdIn(List<UUID> ids);
}

