package tr.kontas.erp.app.notification.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TenantNotificationConfigPayload {
    private String id;
    private boolean allNotificationsEnabled;
    private List<String> disabledChannels;
    private List<String> disabledKeys;
    private String disabledReason;
    private String disabledBy;
    private String disabledAt;
    private String disabledUntil;
}

