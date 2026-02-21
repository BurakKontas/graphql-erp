package tr.kontas.erp.purchase.domain.purchasereturn;

import tr.kontas.erp.core.kernel.domain.model.Identifier;
import java.util.UUID;

public class PurchaseReturnId extends Identifier {
    private PurchaseReturnId(UUID value) {
        super(value);
    }

    public static PurchaseReturnId newId() {
        return new PurchaseReturnId(UUID.randomUUID());
    }

    public static PurchaseReturnId of(UUID value) {
        return new PurchaseReturnId(value);
    }

    public static PurchaseReturnId of(String value) {
        return new PurchaseReturnId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
