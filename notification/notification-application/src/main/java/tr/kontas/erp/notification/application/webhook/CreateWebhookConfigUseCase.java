package tr.kontas.erp.notification.application.webhook;

import tr.kontas.erp.notification.domain.webhook.WebhookConfigId;

public interface CreateWebhookConfigUseCase {
    WebhookConfigId execute(CreateWebhookConfigCommand command);
}

