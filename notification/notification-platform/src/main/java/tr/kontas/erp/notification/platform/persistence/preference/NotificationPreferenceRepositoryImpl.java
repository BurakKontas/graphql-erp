package tr.kontas.erp.notification.platform.persistence.preference;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.preference.NotificationPreference;
import tr.kontas.erp.notification.domain.preference.NotificationPreferenceId;
import tr.kontas.erp.notification.domain.preference.NotificationPreferenceRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationPreferenceRepositoryImpl implements NotificationPreferenceRepository {

    private final JpaNotificationPreferenceRepository jpaRepository;

    @Override
    public void save(NotificationPreference preference) {
        jpaRepository.save(NotificationPreferenceMapper.toEntity(preference));
    }

    @Override
    public Optional<NotificationPreference> findById(NotificationPreferenceId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(NotificationPreferenceMapper::toDomain);
    }

    @Override
    public Optional<NotificationPreference> findByUserAndKey(String userId, String notificationKey, TenantId tenantId) {
        return jpaRepository.findByUserIdAndNotificationKeyAndTenantId(userId, notificationKey, tenantId.asUUID())
                .map(NotificationPreferenceMapper::toDomain);
    }

    @Override
    public List<NotificationPreference> findByUserId(String userId, TenantId tenantId) {
        return jpaRepository.findByUserIdAndTenantId(userId, tenantId.asUUID())
                .stream()
                .map(NotificationPreferenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationPreference> findByIds(List<NotificationPreferenceId> ids) {
        List<UUID> uuids = ids.stream().map(NotificationPreferenceId::asUUID).collect(Collectors.toList());
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(NotificationPreferenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}

