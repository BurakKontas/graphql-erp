package tr.kontas.erp.notification.domain.webhook;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class WebhookConfigId extends Identifier {

    private WebhookConfigId(UUID value) {
        super(value);
    }

    public static WebhookConfigId newId() {
        return new WebhookConfigId(UUID.randomUUID());
    }

    public static WebhookConfigId of(UUID value) {
        return new WebhookConfigId(value);
    }

    public static WebhookConfigId of(String value) {
        return new WebhookConfigId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

