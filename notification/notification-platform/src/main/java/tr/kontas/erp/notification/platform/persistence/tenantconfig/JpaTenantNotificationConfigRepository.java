package tr.kontas.erp.notification.platform.persistence.tenantconfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaTenantNotificationConfigRepository extends JpaRepository<TenantNotificationConfigJpaEntity, UUID> {
    Optional<TenantNotificationConfigJpaEntity> findByTenantId(UUID tenantId);
    Optional<TenantNotificationConfigJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}

