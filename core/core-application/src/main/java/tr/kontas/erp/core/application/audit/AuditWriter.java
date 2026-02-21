package tr.kontas.erp.core.application.audit;

import tr.kontas.erp.core.domain.audit.AuditEntry;

public interface AuditWriter {
    void write(AuditEntry entry);
}

