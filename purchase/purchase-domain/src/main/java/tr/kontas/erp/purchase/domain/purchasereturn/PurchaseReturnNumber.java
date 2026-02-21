package tr.kontas.erp.purchase.domain.purchasereturn;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class PurchaseReturnNumber extends ValueObject {

    private final String value;

    public PurchaseReturnNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PurchaseReturnNumber cannot be empty");
        }
        this.value = value;
    }
}
