package tr.kontas.erp.notification.platform.persistence.inapp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaInAppNotificationRepository extends JpaRepository<InAppNotificationJpaEntity, UUID> {
    List<InAppNotificationJpaEntity> findByUserIdAndTenantIdOrderByCreatedAtDesc(String userId, UUID tenantId);

    @Modifying
    @Query("UPDATE InAppNotificationJpaEntity n SET n.read = true, n.readAt = :readAt WHERE n.userId = :userId AND n.tenantId = :tenantId AND n.read = false")
    void markAllAsRead(@Param("userId") String userId, @Param("tenantId") UUID tenantId, @Param("readAt") Instant readAt);
}

