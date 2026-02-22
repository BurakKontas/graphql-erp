package tr.kontas.erp.app.notification.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WebhookConfigPayload {
    private String id;
    private String companyId;
    private String targetUrl;
    private List<String> eventTypes;
    private String status;
    private int consecutiveFailures;
    private String lastSuccessAt;
}

