package tr.kontas.erp.notification.domain.webhook;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.WebhookStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class WebhookConfig extends AggregateRoot<WebhookConfigId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private String targetUrl;
    private String secretKey;
    private List<String> eventTypes;
    private WebhookStatus status;
    private int consecutiveFailures;
    private Instant lastSuccessAt;

    public WebhookConfig(
            WebhookConfigId id,
            TenantId tenantId,
            CompanyId companyId,
            String targetUrl,
            String secretKey,
            List<String> eventTypes,
            WebhookStatus status,
            int consecutiveFailures,
            Instant lastSuccessAt) {
        super(id);
        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");
        if (targetUrl == null || targetUrl.isBlank()) throw new IllegalArgumentException("targetUrl cannot be blank");
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.targetUrl = targetUrl;
        this.secretKey = secretKey;
        this.eventTypes = eventTypes != null ? new ArrayList<>(eventTypes) : new ArrayList<>();
        this.status = status != null ? status : WebhookStatus.ACTIVE;
        this.consecutiveFailures = consecutiveFailures;
        this.lastSuccessAt = lastSuccessAt;
    }

    public void pause() {
        this.status = WebhookStatus.PAUSED;
    }

    public void resume() {
        this.status = WebhookStatus.ACTIVE;
        this.consecutiveFailures = 0;
    }

    public void recordSuccess() {
        this.consecutiveFailures = 0;
        this.lastSuccessAt = Instant.now();
    }

    public void recordFailure() {
        this.consecutiveFailures++;
        if (this.consecutiveFailures >= 3) {
            this.status = WebhookStatus.PAUSED;
        }
    }

    public void updateUrl(String targetUrl) {
        if (targetUrl == null || targetUrl.isBlank()) throw new IllegalArgumentException("targetUrl cannot be blank");
        this.targetUrl = targetUrl;
    }

    public void updateSecret(String secretKey) {
        this.secretKey = secretKey;
    }

    public void updateEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes != null ? new ArrayList<>(eventTypes) : new ArrayList<>();
    }
}

