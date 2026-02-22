package tr.kontas.erp.notification.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.notification.application.template.*;
import tr.kontas.erp.notification.domain.template.NotificationTemplate;
import tr.kontas.erp.notification.domain.template.NotificationTemplateId;
import tr.kontas.erp.notification.domain.template.NotificationTemplateRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationTemplateService implements
        CreateNotificationTemplateUseCase,
        GetNotificationTemplateByIdUseCase,
        GetNotificationTemplatesUseCase,
        UpdateNotificationTemplateUseCase {

    private final NotificationTemplateRepository repository;

    @Override
    @Transactional
    public NotificationTemplateId execute(CreateNotificationTemplateCommand command) {
        TenantId tenantId = TenantContext.get();
        NotificationTemplateId id = NotificationTemplateId.newId();
        NotificationTemplate template = new NotificationTemplate(
                id,
                tenantId,
                command.notificationKey(),
                command.channel(),
                command.locale(),
                command.subject(),
                command.bodyTemplate(),
                command.systemTemplate(),
                true
        );
        repository.save(template);
        return id;
    }

    @Override
    public NotificationTemplate execute(NotificationTemplateId id) {
        TenantId tenantId = TenantContext.get();
        return repository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Notification template not found: " + id));
    }

    @Override
    public List<NotificationTemplate> execute() {
        TenantId tenantId = TenantContext.get();
        return repository.findByTenantId(tenantId);
    }

    @Override
    @Transactional
    public void execute(UpdateNotificationTemplateCommand command) {
        TenantId tenantId = TenantContext.get();
        NotificationTemplate template = repository.findById(NotificationTemplateId.of(command.templateId()), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Notification template not found: " + command.templateId()));
        template.updateTemplate(command.subject(), command.bodyTemplate());
        repository.save(template);
    }
}

