package tr.kontas.erp.notification.platform.persistence.preference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaNotificationPreferenceRepository extends JpaRepository<NotificationPreferenceJpaEntity, UUID> {
    Optional<NotificationPreferenceJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<NotificationPreferenceJpaEntity> findByUserIdAndNotificationKeyAndTenantId(String userId, String notificationKey, UUID tenantId);
    List<NotificationPreferenceJpaEntity> findByUserIdAndTenantId(String userId, UUID tenantId);
    List<NotificationPreferenceJpaEntity> findByIdIn(List<UUID> ids);
}

