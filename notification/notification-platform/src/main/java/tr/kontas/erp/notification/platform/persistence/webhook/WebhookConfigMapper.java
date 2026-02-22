package tr.kontas.erp.notification.platform.persistence.webhook;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.notification.domain.WebhookStatus;
import tr.kontas.erp.notification.domain.webhook.WebhookConfig;
import tr.kontas.erp.notification.domain.webhook.WebhookConfigId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebhookConfigMapper {

    public static WebhookConfigJpaEntity toEntity(WebhookConfig domain) {
        WebhookConfigJpaEntity entity = new WebhookConfigJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setTargetUrl(domain.getTargetUrl());
        entity.setSecretKey(domain.getSecretKey());
        entity.setEventTypes(domain.getEventTypes() != null ? String.join(",", domain.getEventTypes()) : null);
        entity.setStatus(domain.getStatus().name());
        entity.setConsecutiveFailures(domain.getConsecutiveFailures());
        entity.setLastSuccessAt(domain.getLastSuccessAt());
        return entity;
    }

    public static WebhookConfig toDomain(WebhookConfigJpaEntity entity) {
        List<String> eventTypes = entity.getEventTypes() != null && !entity.getEventTypes().isBlank()
                ? new ArrayList<>(Arrays.asList(entity.getEventTypes().split(",")))
                : new ArrayList<>();
        return new WebhookConfig(
                WebhookConfigId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                entity.getTargetUrl(),
                entity.getSecretKey(),
                eventTypes,
                WebhookStatus.valueOf(entity.getStatus()),
                entity.getConsecutiveFailures(),
                entity.getLastSuccessAt()
        );
    }
}

