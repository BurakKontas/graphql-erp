package tr.kontas.erp.core.platform.service.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.application.audit.AuditQueryService;
import tr.kontas.erp.core.domain.audit.AuditEntry;
import tr.kontas.erp.core.domain.audit.AuditEntryRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditQueryServiceImpl implements AuditQueryService {

    private final AuditEntryRepository auditEntryRepository;

    @Override
    public List<AuditEntry> findByAggregate(String aggregateType, String aggregateId, UUID tenantId) {
        return auditEntryRepository.findByAggregate(aggregateType, aggregateId, tenantId);
    }

    @Override
    public List<AuditEntry> findByUser(String userId, UUID tenantId, Instant from, Instant to) {
        return auditEntryRepository.findByUser(userId, tenantId, from, to);
    }

    @Override
    public List<AuditEntry> findByModule(String moduleName, UUID tenantId, Instant from, Instant to) {
        return auditEntryRepository.findByModule(moduleName, tenantId, from, to);
    }

    @Override
    public List<AuditEntry> findAll(UUID tenantId, Instant from, Instant to, int limit, int offset) {
        return auditEntryRepository.findAll(tenantId, from, to, limit, offset);
    }
}

