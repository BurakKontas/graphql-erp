package tr.kontas.erp.finance.domain.salesinvoice;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface SalesInvoiceRepository {
    void save(SalesInvoice invoice);
    Optional<SalesInvoice> findById(SalesInvoiceId id, TenantId tenantId);
    List<SalesInvoice> findByCompanyId(TenantId tenantId, CompanyId companyId);
    Optional<SalesInvoice> findBySalesOrderId(TenantId tenantId, String salesOrderId);
    List<SalesInvoice> findByIds(List<SalesInvoiceId> ids);
    int findMaxSequenceByTenantId(TenantId tenantId);
}

