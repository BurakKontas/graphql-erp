package tr.kontas.erp.notification.application.webhook;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.util.List;

public record CreateWebhookConfigCommand(
        CompanyId companyId,
        String targetUrl,
        String secretKey,
        List<String> eventTypes
) {
}

