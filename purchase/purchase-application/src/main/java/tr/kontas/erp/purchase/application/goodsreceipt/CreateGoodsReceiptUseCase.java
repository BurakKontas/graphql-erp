package tr.kontas.erp.purchase.application.goodsreceipt;

import tr.kontas.erp.purchase.domain.goodsreceipt.GoodsReceiptId;

public interface CreateGoodsReceiptUseCase {
    GoodsReceiptId execute(CreateGoodsReceiptCommand command);
}

