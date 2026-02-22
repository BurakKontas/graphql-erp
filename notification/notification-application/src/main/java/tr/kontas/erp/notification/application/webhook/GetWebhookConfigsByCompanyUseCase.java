package tr.kontas.erp.notification.application.webhook;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.notification.domain.webhook.WebhookConfig;

import java.util.List;

public interface GetWebhookConfigsByCompanyUseCase {
    List<WebhookConfig> execute(CompanyId companyId);
}

