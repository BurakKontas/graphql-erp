package tr.kontas.erp.notification.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.notification.application.inapp.*;
import tr.kontas.erp.notification.platform.persistence.inapp.InAppNotificationJpaEntity;
import tr.kontas.erp.notification.platform.persistence.inapp.JpaInAppNotificationRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InAppNotificationService implements
        GetInAppNotificationsByUserUseCase,
        MarkNotificationReadUseCase,
        MarkAllNotificationsReadUseCase {

    private final JpaInAppNotificationRepository repository;

    @Override
    public List<InAppNotificationResult> execute(String userId) {
        TenantId tenantId = TenantContext.get();
        return repository.findByUserIdAndTenantIdOrderByCreatedAtDesc(userId, tenantId.asUUID())
                .stream()
                .map(this::toResult)
                .toList();
    }

    @Override
    @Transactional
    public void execute(String notificationId) {
        InAppNotificationJpaEntity entity = repository.findById(UUID.fromString(notificationId))
                .orElseThrow(() -> new IllegalArgumentException("In-app notification not found: " + notificationId));
        entity.setRead(true);
        entity.setReadAt(Instant.now());
        repository.save(entity);
    }

    @Override
    @Transactional
    public void markAllRead(String userId) {
        TenantId tenantId = TenantContext.get();
        repository.markAllAsRead(userId, tenantId.asUUID(), Instant.now());
    }

    private InAppNotificationResult toResult(InAppNotificationJpaEntity entity) {
        return new InAppNotificationResult(
                entity.getId().toString(),
                entity.getUserId(),
                entity.getNotificationKey(),
                entity.getTitle(),
                entity.getBody(),
                entity.getActionUrl(),
                entity.isRead(),
                entity.getReadAt(),
                entity.getCreatedAt()
        );
    }
}

