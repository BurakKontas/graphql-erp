package tr.kontas.erp.core.platform.service.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.application.audit.AuditWriter;
import tr.kontas.erp.core.domain.audit.AuditEntry;
import tr.kontas.erp.core.domain.audit.AuditEntryRepository;

@Service
@RequiredArgsConstructor
public class AuditWriterImpl implements AuditWriter {

    private final AuditEntryRepository auditEntryRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void write(AuditEntry entry) {
        auditEntryRepository.save(entry);
    }
}

