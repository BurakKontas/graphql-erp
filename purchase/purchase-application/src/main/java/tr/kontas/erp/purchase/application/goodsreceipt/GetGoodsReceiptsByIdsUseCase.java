package tr.kontas.erp.purchase.application.goodsreceipt;

import tr.kontas.erp.purchase.domain.goodsreceipt.GoodsReceipt;
import tr.kontas.erp.purchase.domain.goodsreceipt.GoodsReceiptId;

import java.util.List;

public interface GetGoodsReceiptsByIdsUseCase {
    List<GoodsReceipt> execute(List<GoodsReceiptId> ids);
}

