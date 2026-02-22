package tr.kontas.erp.reporting.domain.scheduled;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class ScheduledReportId extends Identifier {

    private ScheduledReportId(UUID value) {
        super(value);
    }

    public static ScheduledReportId newId() {
        return new ScheduledReportId(UUID.randomUUID());
    }

    public static ScheduledReportId of(UUID value) {
        return new ScheduledReportId(value);
    }

    public static ScheduledReportId of(String value) {
        return new ScheduledReportId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

