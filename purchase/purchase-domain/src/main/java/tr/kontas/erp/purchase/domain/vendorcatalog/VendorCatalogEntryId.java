package tr.kontas.erp.purchase.domain.vendorcatalog;

import tr.kontas.erp.core.kernel.domain.model.Identifier;
import java.util.UUID;

public class VendorCatalogEntryId extends Identifier {
    private VendorCatalogEntryId(UUID value) {
        super(value);
    }

    public static VendorCatalogEntryId newId() {
        return new VendorCatalogEntryId(UUID.randomUUID());
    }

    public static VendorCatalogEntryId of(UUID value) {
        return new VendorCatalogEntryId(value);
    }

    public static VendorCatalogEntryId of(String value) {
        return new VendorCatalogEntryId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
