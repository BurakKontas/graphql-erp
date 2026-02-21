package tr.kontas.erp.purchase.domain.goodsreceipt;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class GoodsReceiptNumber extends ValueObject {

    private final String value;

    public GoodsReceiptNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("GoodsReceiptNumber cannot be empty");
        }
        this.value = value;
    }
}
