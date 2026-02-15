package tr.kontas.erp.core.domain.company;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class CompanyId extends Identifier {

    private CompanyId(UUID value) {
        super(value);
    }

    public static CompanyId newId() {
        return new CompanyId(UUID.randomUUID());
    }

    public static CompanyId of(UUID value) {
        return new CompanyId(value);
    }

    public static CompanyId of(String value) {
        return new CompanyId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
