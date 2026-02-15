package tr.kontas.erp.core.domain.employee;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class EmployeeId extends Identifier {

    private EmployeeId(UUID value) {
        super(value);
    }

    public static EmployeeId newId() {
        return new EmployeeId(UUID.randomUUID());
    }

    public static EmployeeId of(UUID value) {
        return new EmployeeId(value);
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
