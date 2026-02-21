package tr.kontas.erp.inventory.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.inventory.application.category.*;
import tr.kontas.erp.inventory.domain.category.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements
        CreateCategoryUseCase,
        GetCategoryByIdUseCase,
        GetCategoriesByCompanyUseCase,
        GetCategoriesByIdsUseCase,
        GetSubCategoriesUseCase,
        RenameCategoryUseCase,
        DeactivateCategoryUseCase,
        ActivateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public CategoryId execute(CreateCategoryCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();

        CategoryId parentId = command.parentCategoryId() != null
                ? CategoryId.of(command.parentCategoryId())
                : null;

        CategoryId id = CategoryId.newId();
        Category category = new Category(
                id,
                tenantId,
                companyId,
                new CategoryName(command.name()),
                parentId
        );

        saveAndPublish(category);
        return id;
    }

    @Override
    public Category execute(CategoryId id) {
        TenantId tenantId = TenantContext.get();
        return categoryRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
    }

    @Override
    public List<Category> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return categoryRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<Category> execute(List<CategoryId> ids) {
        return categoryRepository.findByIds(ids);
    }

    @Override
    public List<Category> getSubCategories(CategoryId parentId) {
        TenantId tenantId = TenantContext.get();
        return categoryRepository.findByParentCategoryId(parentId, tenantId);
    }

    @Override
    @Transactional
    public void execute(String categoryId, String newName) {
        Category category = loadCategory(categoryId);
        category.rename(new CategoryName(newName));
        saveAndPublish(category);
    }

    @Override
    @Transactional
    public void deactivate(String categoryId) {
        Category category = loadCategory(categoryId);
        category.deactivate();
        saveAndPublish(category);
    }

    @Override
    @Transactional
    public void activate(String categoryId) {
        Category category = loadCategory(categoryId);
        category.activate();
        saveAndPublish(category);
    }

    private Category loadCategory(String categoryId) {
        TenantId tenantId = TenantContext.get();
        return categoryRepository.findById(CategoryId.of(categoryId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));
    }

    private void saveAndPublish(Category category) {
        categoryRepository.save(category);
        eventPublisher.publishAll(category.getDomainEvents());
        category.clearDomainEvents();
    }
}
