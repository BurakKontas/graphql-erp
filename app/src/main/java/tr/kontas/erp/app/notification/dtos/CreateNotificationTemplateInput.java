package tr.kontas.erp.app.notification.dtos;

import lombok.Data;

@Data
public class CreateNotificationTemplateInput {
    private String notificationKey;
    private String channel;
    private String locale;
    private String subject;
    private String bodyTemplate;
    private Boolean systemTemplate;
}

