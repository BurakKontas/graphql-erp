package tr.kontas.erp.app.notification.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreateNotificationPreferenceInput {
    private String userId;
    private String notificationKey;
    private List<String> disabledChannels;
    private String preferredLocale;
}

