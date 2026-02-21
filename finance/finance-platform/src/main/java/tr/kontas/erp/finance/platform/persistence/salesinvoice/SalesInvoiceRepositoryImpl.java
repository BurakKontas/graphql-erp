package tr.kontas.erp.finance.platform.persistence.salesinvoice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.salesinvoice.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SalesInvoiceRepositoryImpl implements SalesInvoiceRepository {

    private final JpaSalesInvoiceRepository jpa;

    @Override
    public void save(SalesInvoice invoice) {
        jpa.save(SalesInvoiceMapper.toEntity(invoice));
    }

    @Override
    public Optional<SalesInvoice> findById(SalesInvoiceId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(SalesInvoiceMapper::toDomain);
    }

    @Override
    public List<SalesInvoice> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(SalesInvoiceMapper::toDomain).toList();
    }

    @Override
    public Optional<SalesInvoice> findBySalesOrderId(TenantId tenantId, String salesOrderId) {
        return jpa.findByTenantIdAndSalesOrderId(tenantId.asUUID(), salesOrderId)
                .map(SalesInvoiceMapper::toDomain);
    }

    @Override
    public List<SalesInvoice> findByIds(List<SalesInvoiceId> ids) {
        return jpa.findByIdIn(ids.stream().map(SalesInvoiceId::asUUID).toList())
                .stream().map(SalesInvoiceMapper::toDomain).toList();
    }

    @Override
    public int findMaxSequenceByTenantId(TenantId tenantId) {
        return jpa.findMaxSequenceByTenantId(tenantId.asUUID());
    }
}
