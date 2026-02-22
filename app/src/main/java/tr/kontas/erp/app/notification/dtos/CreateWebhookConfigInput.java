package tr.kontas.erp.app.notification.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreateWebhookConfigInput {
    private String companyId;
    private String targetUrl;
    private String secretKey;
    private List<String> eventTypes;
}

