package tr.kontas.erp.purchase.domain.purchaseorder;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class PurchaseOrderNumber extends ValueObject {

    private final String value;

    public PurchaseOrderNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PurchaseOrderNumber cannot be empty");
        }
        this.value = value;
    }
}
