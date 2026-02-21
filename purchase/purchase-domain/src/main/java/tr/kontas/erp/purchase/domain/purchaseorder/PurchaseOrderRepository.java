package tr.kontas.erp.purchase.domain.purchaseorder;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface PurchaseOrderRepository {
    void save(PurchaseOrder order);
    Optional<PurchaseOrder> findById(PurchaseOrderId id, TenantId tenantId);
    List<PurchaseOrder> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<PurchaseOrder> findByIds(List<PurchaseOrderId> ids);
    int findMaxSequenceByTenantId(TenantId tenantId);
}