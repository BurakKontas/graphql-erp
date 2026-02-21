package tr.kontas.erp.hr.domain.performancecycle;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class PerformanceCycleId extends Identifier {
    private PerformanceCycleId(UUID value) {
        super(value);
    }

    public static PerformanceCycleId newId() {
        return new PerformanceCycleId(UUID.randomUUID());
    }

    public static PerformanceCycleId of(UUID value) {
        return new PerformanceCycleId(value);
    }

    public static PerformanceCycleId of(String value) {
        return new PerformanceCycleId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
