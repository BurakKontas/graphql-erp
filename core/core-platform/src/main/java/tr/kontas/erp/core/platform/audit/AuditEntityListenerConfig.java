package tr.kontas.erp.core.platform.audit;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.application.audit.AuditWriter;

@Component
@RequiredArgsConstructor
public class AuditEntityListenerConfig {

    private final AuditWriter auditWriter;

    @PostConstruct
    public void init() {
        AuditEntityListener.setAuditWriter(auditWriter);
    }
}

