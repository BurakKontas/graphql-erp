package tr.kontas.erp.core.domain.businesspartner;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class BusinessPartnerId extends Identifier {

    private BusinessPartnerId(UUID value) {
        super(value);
    }

    public static BusinessPartnerId newId() {
        return new BusinessPartnerId(UUID.randomUUID());
    }

    public static BusinessPartnerId of(UUID value) {
        return new BusinessPartnerId(value);
    }

    public static BusinessPartnerId of(String value) {
        return new BusinessPartnerId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
