package tr.kontas.erp.notification.application.tenantconfig;

import tr.kontas.erp.notification.domain.tenantconfig.TenantNotificationConfigId;

public interface CreateTenantNotificationConfigUseCase {
    TenantNotificationConfigId execute(CreateTenantNotificationConfigCommand command);
}

