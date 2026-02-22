package tr.kontas.erp.app.notification.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InAppNotificationPayload {
    private String id;
    private String userId;
    private String notificationKey;
    private String title;
    private String body;
    private String actionUrl;
    private boolean read;
    private String readAt;
    private String createdAt;
}

