package tr.kontas.erp.inventory.platform.persistence.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.inventory.domain.category.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JpaCategoryRepository jpaRepository;

    @Override
    public void save(Category category) {
        jpaRepository.save(CategoryMapper.toEntity(category));
    }

    @Override
    public Optional<Category> findById(CategoryId id, TenantId tenantId) {
        return jpaRepository.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(CategoryMapper::toDomain);
    }

    @Override
    public List<Category> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpaRepository.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream()
                .map(CategoryMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findByIds(List<CategoryId> ids) {
        List<UUID> uuids = ids.stream().map(CategoryId::asUUID).collect(Collectors.toList());
        return jpaRepository.findByIdIn(uuids)
                .stream()
                .map(CategoryMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findByParentCategoryId(CategoryId parentId, TenantId tenantId) {
        return jpaRepository.findByParentCategoryIdAndTenantId(parentId.asUUID(), tenantId.asUUID())
                .stream()
                .map(CategoryMapper::toDomain)
                .collect(Collectors.toList());
    }
}
