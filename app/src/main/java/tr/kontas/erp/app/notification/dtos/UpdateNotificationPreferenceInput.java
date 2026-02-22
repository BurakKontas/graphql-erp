package tr.kontas.erp.app.notification.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UpdateNotificationPreferenceInput {
    private String preferenceId;
    private List<String> disabledChannels;
    private String preferredLocale;
}

