package tr.kontas.erp.purchase.domain.goodsreceipt;

import tr.kontas.erp.core.kernel.domain.model.Identifier;
import java.util.UUID;

public class GoodsReceiptId extends Identifier {
    private GoodsReceiptId(UUID value) {
        super(value);
    }

    public static GoodsReceiptId newId() {
        return new GoodsReceiptId(UUID.randomUUID());
    }

    public static GoodsReceiptId of(UUID value) {
        return new GoodsReceiptId(value);
    }

    public static GoodsReceiptId of(String value) {
        return new GoodsReceiptId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
