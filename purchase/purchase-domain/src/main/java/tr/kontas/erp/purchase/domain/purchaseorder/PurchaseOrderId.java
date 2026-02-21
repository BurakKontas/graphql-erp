package tr.kontas.erp.purchase.domain.purchaseorder;

import tr.kontas.erp.core.kernel.domain.model.Identifier;
import java.util.UUID;

public class PurchaseOrderId extends Identifier {
    private PurchaseOrderId(UUID value) {
        super(value);
    }

    public static PurchaseOrderId newId() {
        return new PurchaseOrderId(UUID.randomUUID());
    }

    public static PurchaseOrderId of(UUID value) {
        return new PurchaseOrderId(value);
    }

    public static PurchaseOrderId of(String value) {
        return new PurchaseOrderId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
