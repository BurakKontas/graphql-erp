package tr.kontas.erp.purchase.platform.persistence.vendorinvoice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoice;
import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoiceId;
import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoiceRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VendorInvoiceRepositoryImpl implements VendorInvoiceRepository {

    private final JpaVendorInvoiceRepository jpaRepository;

    @Override
    public void save(VendorInvoice invoice) {
        jpaRepository.save(VendorInvoiceMapper.toEntity(invoice));
    }

    @Override
    public Optional<VendorInvoice> findById(VendorInvoiceId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(VendorInvoiceMapper::toDomain);
    }

    @Override
    public List<VendorInvoice> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(VendorInvoiceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<VendorInvoice> findByIds(List<VendorInvoiceId> ids) {
        List<UUID> uuids = ids.stream().map(VendorInvoiceId::asUUID).toList();
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(VendorInvoiceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int findMaxSequenceByTenantId(TenantId tenantId) {
        return jpaRepository.findMaxSequenceByTenantId(tenantId.asUUID());
    }
}

