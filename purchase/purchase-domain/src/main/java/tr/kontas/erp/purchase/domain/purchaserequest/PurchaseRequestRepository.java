package tr.kontas.erp.purchase.domain.purchaserequest;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface PurchaseRequestRepository {
    void save(PurchaseRequest request);
    Optional<PurchaseRequest> findById(PurchaseRequestId id, TenantId tenantId);
    List<PurchaseRequest> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<PurchaseRequest> findByIds(List<PurchaseRequestId> ids);
    int findMaxSequenceByTenantId(TenantId tenantId);
}