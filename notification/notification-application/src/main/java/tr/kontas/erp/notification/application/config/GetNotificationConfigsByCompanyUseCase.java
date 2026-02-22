package tr.kontas.erp.notification.application.config;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.notification.domain.config.NotificationConfig;

import java.util.List;

public interface GetNotificationConfigsByCompanyUseCase {
    List<NotificationConfig> execute(CompanyId companyId);
}

