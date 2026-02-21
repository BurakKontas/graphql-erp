package tr.kontas.erp.purchase.domain.vendorinvoice;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface VendorInvoiceRepository {
    void save(VendorInvoice invoice);
    Optional<VendorInvoice> findById(VendorInvoiceId id, TenantId tenantId);
    List<VendorInvoice> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<VendorInvoice> findByIds(List<VendorInvoiceId> ids);
    int findMaxSequenceByTenantId(TenantId tenantId);
}