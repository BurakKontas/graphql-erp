package tr.kontas.erp.app.notification.dtos;

import lombok.Data;

@Data
public class UpdateNotificationTemplateInput {
    private String templateId;
    private String subject;
    private String bodyTemplate;
}

