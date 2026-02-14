package tr.kontas.erp.core.kernel.multitenancy;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class TenantId extends Identifier {

    private TenantId(UUID value) {
        super(value);
    }

    public static TenantId of(UUID value) {
        return new TenantId(value);
    }

    public static TenantId of(String value) {
        return new TenantId(UUID.fromString(value));
    }

    public static TenantId newId() {
        return new TenantId(UUID.randomUUID());
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
