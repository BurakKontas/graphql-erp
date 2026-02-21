package tr.kontas.erp.inventory.domain.warehouse;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository {
    void save(Warehouse warehouse);
    Optional<Warehouse> findById(WarehouseId id, TenantId tenantId);
    Optional<Warehouse> findByCode(WarehouseCode code, TenantId tenantId, CompanyId companyId);
    List<Warehouse> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Warehouse> findByIds(List<WarehouseId> ids);
}
