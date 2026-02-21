package tr.kontas.erp.hr.domain.payrollrun;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class PayrollRunId extends Identifier {
    private PayrollRunId(UUID value) {
        super(value);
    }

    public static PayrollRunId newId() {
        return new PayrollRunId(UUID.randomUUID());
    }

    public static PayrollRunId of(UUID value) {
        return new PayrollRunId(value);
    }

    public static PayrollRunId of(String value) {
        return new PayrollRunId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
