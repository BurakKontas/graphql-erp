package tr.kontas.erp.notification.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.notification.application.preference.*;
import tr.kontas.erp.notification.domain.NotificationChannel;
import tr.kontas.erp.notification.domain.preference.NotificationPreference;
import tr.kontas.erp.notification.domain.preference.NotificationPreferenceId;
import tr.kontas.erp.notification.domain.preference.NotificationPreferenceRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationPreferenceService implements
        CreateNotificationPreferenceUseCase,
        GetNotificationPreferencesByUserUseCase,
        UpdateNotificationPreferenceUseCase {

    private final NotificationPreferenceRepository repository;

    @Override
    @Transactional
    public NotificationPreferenceId execute(CreateNotificationPreferenceCommand command) {
        TenantId tenantId = TenantContext.get();
        NotificationPreferenceId id = NotificationPreferenceId.newId();

        Set<NotificationChannel> disabled = command.disabledChannels() != null
                ? command.disabledChannels().stream().map(NotificationChannel::valueOf).collect(Collectors.toSet())
                : new HashSet<>();

        NotificationPreference preference = new NotificationPreference(
                id,
                tenantId,
                command.userId(),
                command.notificationKey(),
                disabled,
                command.preferredLocale()
        );
        repository.save(preference);
        return id;
    }

    @Override
    public List<NotificationPreference> execute(String userId) {
        TenantId tenantId = TenantContext.get();
        return repository.findByUserId(userId, tenantId);
    }

    @Override
    @Transactional
    public void execute(UpdateNotificationPreferenceCommand command) {
        TenantId tenantId = TenantContext.get();
        NotificationPreference preference = repository.findById(NotificationPreferenceId.of(command.preferenceId()), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Notification preference not found: " + command.preferenceId()));

        Set<NotificationChannel> newDisabled = command.disabledChannels() != null
                ? command.disabledChannels().stream().map(NotificationChannel::valueOf).collect(Collectors.toSet())
                : new HashSet<>();

        for (NotificationChannel ch : NotificationChannel.values()) {
            if (newDisabled.contains(ch)) {
                preference.disableChannel(ch);
            } else {
                preference.enableChannel(ch);
            }
        }
        if (command.preferredLocale() != null) {
            preference.updateLocale(command.preferredLocale());
        }
        repository.save(preference);
    }
}

