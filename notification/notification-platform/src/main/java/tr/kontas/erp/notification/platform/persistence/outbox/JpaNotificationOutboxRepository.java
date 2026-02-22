package tr.kontas.erp.notification.platform.persistence.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaNotificationOutboxRepository extends JpaRepository<NotificationOutboxJpaEntity, UUID> {
    List<NotificationOutboxJpaEntity> findByStatusAndRetryCountLessThan(String status, int maxRetry);
    List<NotificationOutboxJpaEntity> findByTenantIdOrderByCreatedAtDesc(UUID tenantId);
}

