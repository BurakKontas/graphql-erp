package tr.kontas.erp.purchase.application.goodsreceipt;

import tr.kontas.erp.purchase.domain.goodsreceipt.GoodsReceipt;
import tr.kontas.erp.purchase.domain.goodsreceipt.GoodsReceiptId;

public interface GetGoodsReceiptByIdUseCase {
    GoodsReceipt execute(GoodsReceiptId id);
}

