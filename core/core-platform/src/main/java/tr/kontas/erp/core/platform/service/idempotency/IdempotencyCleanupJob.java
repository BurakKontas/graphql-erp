package tr.kontas.erp.core.platform.service.idempotency;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import tr.kontas.erp.core.platform.persistence.idempotency.JpaIdempotencyRepository;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBooleanProperty( // disable if you wanna store all idempotent responses
    name = "idempotency.cleanup.enabled",
    matchIfMissing = true
)
public class IdempotencyCleanupJob {

    private final JpaIdempotencyRepository jpaIdempotencyRepository;

    // Run every 5 minutes by default; configurable with property if needed
    @Transactional
    @Scheduled(fixedDelayString = "${idempotency.cleanup.delay-ms:300000}")
    public void cleanup() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(1);

        try {
            jpaIdempotencyRepository.removeExpired(cutoff);
            log.debug("IdempotencyCleanup: executed removeExpired with cutoff={}", cutoff);
        } catch (Exception e) {
            log.error("Failed to cleanup idempotencies", e);
        }
    }
}
