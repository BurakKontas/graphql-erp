package tr.kontas.erp.app.notification.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreateNotificationConfigInput {
    private String companyId;
    private String eventType;
    private String notificationKey;
    private String description;
    private List<String> enabledChannels;
    private String deliveryTiming;
    private String cronExpression;
    private List<String> recipientRoles;
    private Boolean userOverridable;
}

