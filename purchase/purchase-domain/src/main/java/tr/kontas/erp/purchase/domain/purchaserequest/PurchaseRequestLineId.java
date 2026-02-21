package tr.kontas.erp.purchase.domain.purchaserequest;

import tr.kontas.erp.core.kernel.domain.model.Identifier;
import java.util.UUID;

public class PurchaseRequestLineId extends Identifier {
    private PurchaseRequestLineId(UUID value) {
        super(value);
    }

    public static PurchaseRequestLineId newId() {
        return new PurchaseRequestLineId(UUID.randomUUID());
    }

    public static PurchaseRequestLineId of(UUID value) {
        return new PurchaseRequestLineId(value);
    }

    public static PurchaseRequestLineId of(String value) {
        return new PurchaseRequestLineId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
