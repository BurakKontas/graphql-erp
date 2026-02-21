package tr.kontas.erp.purchase.application.purchasereturn;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.purchase.domain.purchasereturn.PurchaseReturn;

import java.util.List;

public interface GetPurchaseReturnsByCompanyUseCase {
    List<PurchaseReturn> execute(CompanyId companyId);
}

