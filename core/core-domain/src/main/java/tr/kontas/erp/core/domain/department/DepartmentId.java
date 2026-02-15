package tr.kontas.erp.core.domain.department;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class DepartmentId extends Identifier {

    private DepartmentId(UUID value) {
        super(value);
    }

    public static DepartmentId newId() {
        return new DepartmentId(UUID.randomUUID());
    }

    public static DepartmentId of(UUID value) {
        return new DepartmentId(value);
    }

    public static DepartmentId of(String value) {
        return new DepartmentId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
