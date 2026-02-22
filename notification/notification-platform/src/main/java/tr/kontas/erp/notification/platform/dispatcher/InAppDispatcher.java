package tr.kontas.erp.notification.platform.dispatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.notification.domain.NotificationChannel;
import tr.kontas.erp.notification.platform.persistence.inapp.InAppNotificationJpaEntity;
import tr.kontas.erp.notification.platform.persistence.inapp.JpaInAppNotificationRepository;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InAppDispatcher implements ChannelDispatcher {

    private final JpaInAppNotificationRepository inAppRepository;

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.IN_APP;
    }

    @Override
    public void dispatch(String recipientUserId, String subject, String body) {
        TenantId tenantId = TenantContext.get();
        InAppNotificationJpaEntity entity = new InAppNotificationJpaEntity();
        entity.setId(UUID.randomUUID());
        entity.setTenantId(tenantId.asUUID());
        entity.setUserId(recipientUserId);
        entity.setNotificationKey("SYSTEM");
        entity.setTitle(subject);
        entity.setBody(body);
        entity.setRead(false);
        entity.setCreatedAt(Instant.now());
        inAppRepository.save(entity);
        log.info("[IN_APP] Notification saved for user: {}", recipientUserId);
    }
}

