package tr.kontas.erp.hr.domain.performancereview;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class PerformanceReviewId extends Identifier {
    private PerformanceReviewId(UUID value) {
        super(value);
    }

    public static PerformanceReviewId newId() {
        return new PerformanceReviewId(UUID.randomUUID());
    }

    public static PerformanceReviewId of(UUID value) {
        return new PerformanceReviewId(value);
    }

    public static PerformanceReviewId of(String value) {
        return new PerformanceReviewId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
