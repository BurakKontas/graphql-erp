package tr.kontas.erp.app.notification.dtos;

import lombok.Data;

@Data
public class UpdateTenantNotificationConfigInput {
    private boolean allNotificationsEnabled;
    private String disabledReason;
    private String disabledUntil;
}

