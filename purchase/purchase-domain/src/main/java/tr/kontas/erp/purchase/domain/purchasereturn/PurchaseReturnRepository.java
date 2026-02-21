package tr.kontas.erp.purchase.domain.purchasereturn;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface PurchaseReturnRepository {
    void save(PurchaseReturn ret);
    Optional<PurchaseReturn> findById(PurchaseReturnId id, TenantId tenantId);
    List<PurchaseReturn> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<PurchaseReturn> findByIds(List<PurchaseReturnId> ids);
    int findMaxSequenceByTenantId(TenantId tenantId);
}