package tr.kontas.erp.notification.platform.persistence.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.NotificationChannel;
import tr.kontas.erp.notification.domain.template.NotificationTemplate;
import tr.kontas.erp.notification.domain.template.NotificationTemplateId;
import tr.kontas.erp.notification.domain.template.NotificationTemplateRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationTemplateRepositoryImpl implements NotificationTemplateRepository {

    private final JpaNotificationTemplateRepository jpaRepository;

    @Override
    public void save(NotificationTemplate template) {
        jpaRepository.save(NotificationTemplateMapper.toEntity(template));
    }

    @Override
    public Optional<NotificationTemplate> findById(NotificationTemplateId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(NotificationTemplateMapper::toDomain);
    }

    @Override
    public Optional<NotificationTemplate> findByKeyAndChannel(String notificationKey, NotificationChannel channel, TenantId tenantId) {
        return jpaRepository.findByNotificationKeyAndChannelAndTenantId(notificationKey, channel.name(), tenantId.asUUID())
                .map(NotificationTemplateMapper::toDomain);
    }

    @Override
    public List<NotificationTemplate> findByTenantId(TenantId tenantId) {
        return jpaRepository.findByTenantId(tenantId.asUUID())
                .stream()
                .map(NotificationTemplateMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationTemplate> findByIds(List<NotificationTemplateId> ids) {
        List<UUID> uuids = ids.stream().map(NotificationTemplateId::asUUID).collect(Collectors.toList());
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(NotificationTemplateMapper::toDomain)
                .collect(Collectors.toList());
    }
}

