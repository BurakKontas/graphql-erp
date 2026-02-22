package tr.kontas.erp.notification.platform.persistence.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.config.NotificationConfig;
import tr.kontas.erp.notification.domain.config.NotificationConfigId;
import tr.kontas.erp.notification.domain.config.NotificationConfigRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationConfigRepositoryImpl implements NotificationConfigRepository {

    private final JpaNotificationConfigRepository jpaRepository;

    @Override
    public void save(NotificationConfig config) {
        jpaRepository.save(NotificationConfigMapper.toEntity(config));
    }

    @Override
    public Optional<NotificationConfig> findById(NotificationConfigId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(NotificationConfigMapper::toDomain);
    }

    @Override
    public Optional<NotificationConfig> findByEventTypeAndCompany(String eventType, TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByEventTypeAndTenantIdAndCompanyId(eventType, tenantId.asUUID(), companyId.asUUID())
                .map(NotificationConfigMapper::toDomain);
    }

    @Override
    public List<NotificationConfig> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(NotificationConfigMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationConfig> findByIds(List<NotificationConfigId> ids) {
        List<UUID> uuids = ids.stream().map(NotificationConfigId::asUUID).collect(Collectors.toList());
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(NotificationConfigMapper::toDomain)
                .collect(Collectors.toList());
    }
}

