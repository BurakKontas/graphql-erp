package tr.kontas.erp.purchase.domain.purchasereturn;

import tr.kontas.erp.core.kernel.domain.model.Identifier;
import java.util.UUID;

public class PurchaseReturnLineId extends Identifier {
    private PurchaseReturnLineId(UUID value) {
        super(value);
    }

    public static PurchaseReturnLineId newId() {
        return new PurchaseReturnLineId(UUID.randomUUID());
    }

    public static PurchaseReturnLineId of(UUID value) {
        return new PurchaseReturnLineId(value);
    }

    public static PurchaseReturnLineId of(String value) {
        return new PurchaseReturnLineId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
