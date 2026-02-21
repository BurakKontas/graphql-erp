package tr.kontas.erp.core.platform.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.audit.AuditEntryRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditCleanupJob {

    private final AuditEntryRepository auditEntryRepository;

    private static final Map<String, Integer> DEFAULT_RETENTION_DAYS = Map.of(
            "FINANCE", 3650,
            "HR", 2555,
            "SALES", 1825,
            "INVENTORY", 1825,
            "SHIPMENT", 1825,
            "PURCHASE", 1825,
            "CRM", 1095
    );

    private static final int DEFAULT_DAYS = 365;

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanup() {
        log.info("Starting audit cleanup job");
        for (Map.Entry<String, Integer> entry : DEFAULT_RETENTION_DAYS.entrySet()) {
            Instant cutoff = Instant.now().minus(entry.getValue(), ChronoUnit.DAYS);
            auditEntryRepository.deleteOlderThan(cutoff, entry.getKey());
            log.info("Cleaned audit entries for module {} older than {} days", entry.getKey(), entry.getValue());
        }
        log.info("Audit cleanup job completed");
    }
}

