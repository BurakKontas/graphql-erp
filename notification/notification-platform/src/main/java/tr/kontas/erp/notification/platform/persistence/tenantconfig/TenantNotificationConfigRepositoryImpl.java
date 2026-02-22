package tr.kontas.erp.notification.platform.persistence.tenantconfig;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.tenantconfig.TenantNotificationConfig;
import tr.kontas.erp.notification.domain.tenantconfig.TenantNotificationConfigId;
import tr.kontas.erp.notification.domain.tenantconfig.TenantNotificationConfigRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TenantNotificationConfigRepositoryImpl implements TenantNotificationConfigRepository {

    private final JpaTenantNotificationConfigRepository jpaRepository;

    @Override
    public void save(TenantNotificationConfig config) {
        jpaRepository.save(TenantNotificationConfigMapper.toEntity(config));
    }

    @Override
    public Optional<TenantNotificationConfig> findByTenantId(TenantId tenantId) {
        return jpaRepository.findByTenantId(tenantId.asUUID())
                .map(TenantNotificationConfigMapper::toDomain);
    }

    @Override
    public Optional<TenantNotificationConfig> findById(TenantNotificationConfigId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(TenantNotificationConfigMapper::toDomain);
    }
}

