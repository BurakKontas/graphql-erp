package tr.kontas.erp.purchase.domain.purchaserequest;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class PurchaseRequestNumber extends ValueObject {

    private final String value;

    public PurchaseRequestNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PurchaseRequestNumber cannot be empty");
        }
        this.value = value;
    }
}
