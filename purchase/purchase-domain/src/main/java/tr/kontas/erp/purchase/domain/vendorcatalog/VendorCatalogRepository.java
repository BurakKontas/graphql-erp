package tr.kontas.erp.purchase.domain.vendorcatalog;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface VendorCatalogRepository {
    void save(VendorCatalog catalog);
    Optional<VendorCatalog> findById(VendorCatalogId id, TenantId tenantId);
    List<VendorCatalog> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<VendorCatalog> findByIds(List<VendorCatalogId> ids);
}