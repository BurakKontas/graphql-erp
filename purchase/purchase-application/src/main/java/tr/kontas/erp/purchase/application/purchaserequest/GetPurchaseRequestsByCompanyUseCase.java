package tr.kontas.erp.purchase.application.purchaserequest;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequest;

import java.util.List;

public interface GetPurchaseRequestsByCompanyUseCase {
    List<PurchaseRequest> execute(CompanyId companyId);
}

