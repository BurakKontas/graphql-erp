package tr.kontas.erp.reporting.domain.definition;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class ReportDefinitionId extends Identifier {

    private ReportDefinitionId(UUID value) {
        super(value);
    }

    public static ReportDefinitionId newId() {
        return new ReportDefinitionId(UUID.randomUUID());
    }

    public static ReportDefinitionId of(UUID value) {
        return new ReportDefinitionId(value);
    }

    public static ReportDefinitionId of(String value) {
        return new ReportDefinitionId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

