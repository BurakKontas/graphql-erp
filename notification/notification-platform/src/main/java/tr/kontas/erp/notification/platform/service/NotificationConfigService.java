package tr.kontas.erp.notification.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.notification.application.config.*;
import tr.kontas.erp.notification.domain.DeliveryTiming;
import tr.kontas.erp.notification.domain.NotificationChannel;
import tr.kontas.erp.notification.domain.config.NotificationConfig;
import tr.kontas.erp.notification.domain.config.NotificationConfigId;
import tr.kontas.erp.notification.domain.config.NotificationConfigRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationConfigService implements
        CreateNotificationConfigUseCase,
        GetNotificationConfigByIdUseCase,
        GetNotificationConfigsByCompanyUseCase,
        GetNotificationConfigsByIdsUseCase {

    private final NotificationConfigRepository repository;

    @Override
    @Transactional
    public NotificationConfigId execute(CreateNotificationConfigCommand command) {
        TenantId tenantId = TenantContext.get();
        NotificationConfigId id = NotificationConfigId.newId();

        Set<NotificationChannel> channels = command.enabledChannels() != null
                ? command.enabledChannels().stream().map(NotificationChannel::valueOf).collect(Collectors.toSet())
                : new HashSet<>();

        NotificationConfig config = new NotificationConfig(
                id,
                tenantId,
                command.companyId(),
                command.eventType(),
                command.notificationKey(),
                command.description(),
                channels,
                command.deliveryTiming() != null ? command.deliveryTiming() : DeliveryTiming.IMMEDIATE,
                command.cronExpression(),
                command.recipientRoles() != null ? new HashSet<>(command.recipientRoles()) : new HashSet<>(),
                command.userOverridable(),
                true
        );

        repository.save(config);
        return id;
    }

    @Override
    public NotificationConfig execute(NotificationConfigId id) {
        TenantId tenantId = TenantContext.get();
        return repository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Notification config not found: " + id));
    }

    @Override
    public List<NotificationConfig> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return repository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<NotificationConfig> execute(List<NotificationConfigId> ids) {
        return repository.findByIds(ids);
    }
}

