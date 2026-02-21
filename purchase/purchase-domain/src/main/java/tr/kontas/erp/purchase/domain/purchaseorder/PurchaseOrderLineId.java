package tr.kontas.erp.purchase.domain.purchaseorder;

import tr.kontas.erp.core.kernel.domain.model.Identifier;
import java.util.UUID;

public class PurchaseOrderLineId extends Identifier {
    private PurchaseOrderLineId(UUID value) {
        super(value);
    }

    public static PurchaseOrderLineId newId() {
        return new PurchaseOrderLineId(UUID.randomUUID());
    }

    public static PurchaseOrderLineId of(UUID value) {
        return new PurchaseOrderLineId(value);
    }

    public static PurchaseOrderLineId of(String value) {
        return new PurchaseOrderLineId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
