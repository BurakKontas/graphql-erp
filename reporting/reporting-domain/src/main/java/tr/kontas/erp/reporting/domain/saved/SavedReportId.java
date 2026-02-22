package tr.kontas.erp.reporting.domain.saved;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class SavedReportId extends Identifier {

    private SavedReportId(UUID value) {
        super(value);
    }

    public static SavedReportId newId() {
        return new SavedReportId(UUID.randomUUID());
    }

    public static SavedReportId of(UUID value) {
        return new SavedReportId(value);
    }

    public static SavedReportId of(String value) {
        return new SavedReportId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

