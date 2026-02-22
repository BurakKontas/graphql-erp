package tr.kontas.erp.notification.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.notification.application.tenantconfig.*;
import tr.kontas.erp.notification.domain.tenantconfig.TenantNotificationConfig;
import tr.kontas.erp.notification.domain.tenantconfig.TenantNotificationConfigId;
import tr.kontas.erp.notification.domain.tenantconfig.TenantNotificationConfigRepository;

@Service
@RequiredArgsConstructor
public class TenantNotificationConfigService implements
        CreateTenantNotificationConfigUseCase,
        GetTenantNotificationConfigUseCase,
        UpdateTenantNotificationConfigUseCase {

    private final TenantNotificationConfigRepository repository;

    @Override
    @Transactional
    public TenantNotificationConfigId execute(CreateTenantNotificationConfigCommand command) {
        TenantId tenantId = TenantContext.get();
        TenantNotificationConfigId id = TenantNotificationConfigId.newId();
        TenantNotificationConfig config = TenantNotificationConfig.create(id, tenantId);
        repository.save(config);
        return id;
    }

    @Override
    public TenantNotificationConfig execute() {
        TenantId tenantId = TenantContext.get();
        return repository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant notification config not found"));
    }

    @Override
    @Transactional
    public void execute(UpdateTenantNotificationConfigCommand command) {
        TenantId tenantId = TenantContext.get();
        TenantNotificationConfig config = repository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant notification config not found"));
        if (command.allNotificationsEnabled()) {
            config.enableAll(null);
        } else {
            config.disableAll(command.disabledReason(), null, command.disabledUntil());
        }
        repository.save(config);
    }
}

