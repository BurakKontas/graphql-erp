package tr.kontas.erp.app.notification.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NotificationConfigPayload {
    private String id;
    private String companyId;
    private String eventType;
    private String notificationKey;
    private String description;
    private List<String> enabledChannels;
    private String deliveryTiming;
    private String cronExpression;
    private List<String> recipientRoles;
    private boolean userOverridable;
    private boolean active;
}

