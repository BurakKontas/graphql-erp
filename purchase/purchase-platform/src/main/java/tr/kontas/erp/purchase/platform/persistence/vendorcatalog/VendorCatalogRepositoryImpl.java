package tr.kontas.erp.purchase.platform.persistence.vendorcatalog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.vendorcatalog.VendorCatalog;
import tr.kontas.erp.purchase.domain.vendorcatalog.VendorCatalogId;
import tr.kontas.erp.purchase.domain.vendorcatalog.VendorCatalogRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VendorCatalogRepositoryImpl implements VendorCatalogRepository {

    private final JpaVendorCatalogRepository jpaRepository;

    @Override
    public void save(VendorCatalog catalog) {
        jpaRepository.save(VendorCatalogMapper.toEntity(catalog));
    }

    @Override
    public Optional<VendorCatalog> findById(VendorCatalogId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(VendorCatalogMapper::toDomain);
    }

    @Override
    public List<VendorCatalog> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(VendorCatalogMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<VendorCatalog> findByIds(List<VendorCatalogId> ids) {
        List<UUID> uuids = ids.stream().map(VendorCatalogId::asUUID).toList();
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(VendorCatalogMapper::toDomain)
                .collect(Collectors.toList());
    }
}

