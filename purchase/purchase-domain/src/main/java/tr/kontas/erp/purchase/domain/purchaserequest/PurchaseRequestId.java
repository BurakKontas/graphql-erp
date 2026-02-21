package tr.kontas.erp.purchase.domain.purchaserequest;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class PurchaseRequestId extends Identifier {

    private PurchaseRequestId(UUID value) {
        super(value);
    }


    public static PurchaseRequestId newId() {
        return new PurchaseRequestId(UUID.randomUUID());
    }


    public static PurchaseRequestId of(UUID value) {
        return new PurchaseRequestId(value);
    }


    public static PurchaseRequestId of(String value) {
        return new PurchaseRequestId(UUID.fromString(value));
    }


    public UUID asUUID() {
        return (UUID) getValue();
    }
}

