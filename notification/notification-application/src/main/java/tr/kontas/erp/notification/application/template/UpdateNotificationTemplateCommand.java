package tr.kontas.erp.notification.application.template;

public record UpdateNotificationTemplateCommand(
        String templateId,
        String subject,
        String bodyTemplate
) {
}

