package tr.kontas.erp.notification.application.config;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.notification.domain.DeliveryTiming;

import java.util.Set;

public record CreateNotificationConfigCommand(
        CompanyId companyId,
        String eventType,
        String notificationKey,
        String description,
        Set<String> enabledChannels,
        DeliveryTiming deliveryTiming,
        String cronExpression,
        Set<String> recipientRoles,
        boolean userOverridable
) {
}

