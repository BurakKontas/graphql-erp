package tr.kontas.erp.notification.platform.persistence.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaNotificationTemplateRepository extends JpaRepository<NotificationTemplateJpaEntity, UUID> {
    Optional<NotificationTemplateJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<NotificationTemplateJpaEntity> findByNotificationKeyAndChannelAndTenantId(String notificationKey, String channel, UUID tenantId);
    List<NotificationTemplateJpaEntity> findByTenantId(UUID tenantId);
    List<NotificationTemplateJpaEntity> findByIdIn(List<UUID> ids);
}

