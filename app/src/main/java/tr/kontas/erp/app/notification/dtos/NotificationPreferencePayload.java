package tr.kontas.erp.app.notification.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NotificationPreferencePayload {
    private String id;
    private String userId;
    private String notificationKey;
    private List<String> disabledChannels;
    private String preferredLocale;
}

