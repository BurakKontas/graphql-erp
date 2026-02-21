package tr.kontas.erp.hr.domain.payrollconfig;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class PayrollConfigId extends Identifier {
    private PayrollConfigId(UUID value) {
        super(value);
    }

    public static PayrollConfigId newId() {
        return new PayrollConfigId(UUID.randomUUID());
    }

    public static PayrollConfigId of(UUID value) {
        return new PayrollConfigId(value);
    }

    public static PayrollConfigId of(String value) {
        return new PayrollConfigId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
