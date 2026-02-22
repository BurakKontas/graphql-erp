package tr.kontas.erp.notification.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.notification.application.webhook.*;
import tr.kontas.erp.notification.domain.WebhookStatus;
import tr.kontas.erp.notification.domain.webhook.WebhookConfig;
import tr.kontas.erp.notification.domain.webhook.WebhookConfigId;
import tr.kontas.erp.notification.domain.webhook.WebhookConfigRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebhookConfigService implements
        CreateWebhookConfigUseCase,
        GetWebhookConfigByIdUseCase,
        GetWebhookConfigsByCompanyUseCase {

    private final WebhookConfigRepository repository;

    @Override
    @Transactional
    public WebhookConfigId execute(CreateWebhookConfigCommand command) {
        TenantId tenantId = TenantContext.get();
        WebhookConfigId id = WebhookConfigId.newId();
        WebhookConfig config = new WebhookConfig(
                id,
                tenantId,
                command.companyId(),
                command.targetUrl(),
                command.secretKey(),
                command.eventTypes(),
                WebhookStatus.ACTIVE,
                0,
                null
        );
        repository.save(config);
        return id;
    }

    @Override
    public WebhookConfig execute(WebhookConfigId id) {
        TenantId tenantId = TenantContext.get();
        return repository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Webhook config not found: " + id));
    }

    @Override
    public List<WebhookConfig> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return repository.findByCompanyId(tenantId, companyId);
    }
}

