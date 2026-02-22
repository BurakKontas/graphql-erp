package tr.kontas.erp.notification.application.webhook;

import tr.kontas.erp.notification.domain.webhook.WebhookConfig;
import tr.kontas.erp.notification.domain.webhook.WebhookConfigId;

public interface GetWebhookConfigByIdUseCase {
    WebhookConfig execute(WebhookConfigId id);
}

