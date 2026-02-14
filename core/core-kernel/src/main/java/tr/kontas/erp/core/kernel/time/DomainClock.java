package tr.kontas.erp.core.kernel.time;

import java.time.Instant;

public interface DomainClock {
    default Instant now() {
        return Instant.now();
    }
}
