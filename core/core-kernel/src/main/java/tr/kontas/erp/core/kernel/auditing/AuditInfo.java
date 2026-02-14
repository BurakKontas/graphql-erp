package tr.kontas.erp.core.kernel.auditing;

import lombok.Getter;

import java.time.Instant;

@Getter
public class AuditInfo {

    private final Instant createdAt;
    private Instant modifiedAt;

    public AuditInfo(Instant createdAt) {
        this.createdAt = createdAt;
        this.modifiedAt = createdAt;
    }

    public void markModified() {
        this.modifiedAt = Instant.now();
    }

}
