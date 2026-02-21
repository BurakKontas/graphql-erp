package tr.kontas.erp.inventory.domain.item;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    void save(Item item);
    Optional<Item> findById(ItemId id, TenantId tenantId);
    Optional<Item> findByCode(ItemCode code, TenantId tenantId, CompanyId companyId);
    List<Item> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Item> findByIds(List<ItemId> ids);
}
