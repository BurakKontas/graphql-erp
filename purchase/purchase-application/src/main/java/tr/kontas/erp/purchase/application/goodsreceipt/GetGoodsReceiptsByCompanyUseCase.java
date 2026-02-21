package tr.kontas.erp.purchase.application.goodsreceipt;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.purchase.domain.goodsreceipt.GoodsReceipt;

import java.util.List;

public interface GetGoodsReceiptsByCompanyUseCase {
    List<GoodsReceipt> execute(CompanyId companyId);
}

