package tr.kontas.erp.purchase.domain.goodsreceipt;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface GoodsReceiptRepository {
    void save(GoodsReceipt receipt);
    Optional<GoodsReceipt> findById(GoodsReceiptId id, TenantId tenantId);
    List<GoodsReceipt> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<GoodsReceipt> findByIds(List<GoodsReceiptId> ids);
    int findMaxSequenceByTenantId(TenantId tenantId);
}