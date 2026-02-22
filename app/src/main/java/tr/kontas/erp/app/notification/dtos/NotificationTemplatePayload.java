package tr.kontas.erp.app.notification.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationTemplatePayload {
    private String id;
    private String notificationKey;
    private String channel;
    private String locale;
    private String subject;
    private String bodyTemplate;
    private boolean systemTemplate;
    private boolean active;
}

