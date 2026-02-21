package tr.kontas.erp.inventory.domain.category;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    void save(Category category);
    Optional<Category> findById(CategoryId id, TenantId tenantId);
    List<Category> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Category> findByIds(List<CategoryId> ids);
}
