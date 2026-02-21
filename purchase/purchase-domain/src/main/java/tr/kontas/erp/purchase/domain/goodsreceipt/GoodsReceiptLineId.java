package tr.kontas.erp.purchase.domain.goodsreceipt;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class GoodsReceiptLineId extends Identifier {

    private GoodsReceiptLineId(UUID value) {
        super(value);
    }


    public static GoodsReceiptLineId newId() {
        return new GoodsReceiptLineId(UUID.randomUUID());
    }


    public static GoodsReceiptLineId of(UUID value) {
        return new GoodsReceiptLineId(value);
    }


    public static GoodsReceiptLineId of(String value) {
        return new GoodsReceiptLineId(UUID.fromString(value));
    }


    public UUID asUUID() {
        return (UUID) getValue();
    }
}
