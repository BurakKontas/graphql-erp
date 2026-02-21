package tr.kontas.erp.purchase.domain.vendorcatalog;

import tr.kontas.erp.core.kernel.domain.model.Identifier;
import java.util.UUID;

public class VendorCatalogId extends Identifier {
    private VendorCatalogId(UUID value) {
        super(value);
    }

    public static VendorCatalogId newId() {
        return new VendorCatalogId(UUID.randomUUID());
    }

    public static VendorCatalogId of(UUID value) {
        return new VendorCatalogId(value);
    }

    public static VendorCatalogId of(String value) {
        return new VendorCatalogId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
