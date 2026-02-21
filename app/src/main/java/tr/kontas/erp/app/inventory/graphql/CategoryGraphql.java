package tr.kontas.erp.app.inventory.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.inventory.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.inventory.application.category.*;
import tr.kontas.erp.inventory.domain.category.Category;
import tr.kontas.erp.inventory.domain.category.CategoryId;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class CategoryGraphql {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final GetCategoriesByCompanyUseCase getCategoriesByCompanyUseCase;
    private final GetSubCategoriesUseCase getSubCategoriesUseCase;
    private final RenameCategoryUseCase renameCategoryUseCase;
    private final DeactivateCategoryUseCase deactivateCategoryUseCase;
    private final ActivateCategoryUseCase activateCategoryUseCase;

    public static CategoryPayload toPayload(Category c) {
        return new CategoryPayload(
                c.getId().asUUID().toString(),
                c.getCompanyId().asUUID().toString(),
                c.getName().getValue(),
                c.getParentCategoryId() != null ? c.getParentCategoryId().asUUID().toString() : null,
                c.isActive()
        );
    }

    // ─── Queries ───

    @DgsQuery
    public CategoryPayload category(@InputArgument("id") String id) {
        Category c = getCategoryByIdUseCase.execute(CategoryId.of(id));
        return toPayload(c);
    }

    @DgsQuery
    public List<CategoryPayload> categories(@InputArgument("companyId") String companyId) {
        return getCategoriesByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(CategoryGraphql::toPayload).toList();
    }

    // ─── Mutations ───

    @DgsMutation
    public CategoryPayload createCategory(@InputArgument("input") CreateCategoryInput input) {
        CreateCategoryCommand cmd = new CreateCategoryCommand(
                CompanyId.of(input.getCompanyId()),
                input.getName(),
                input.getParentCategoryId()
        );
        CategoryId id = createCategoryUseCase.execute(cmd);
        return toPayload(getCategoryByIdUseCase.execute(id));
    }

    @DgsMutation
    public CategoryPayload renameCategory(@InputArgument("input") RenameCategoryInput input) {
        renameCategoryUseCase.execute(input.getCategoryId(), input.getName());
        return toPayload(getCategoryByIdUseCase.execute(CategoryId.of(input.getCategoryId())));
    }

    @DgsMutation
    public CategoryPayload deactivateCategory(@InputArgument("categoryId") String categoryId) {
        deactivateCategoryUseCase.deactivate(categoryId);
        return toPayload(getCategoryByIdUseCase.execute(CategoryId.of(categoryId)));
    }

    @DgsMutation
    public CategoryPayload activateCategory(@InputArgument("categoryId") String categoryId) {
        activateCategoryUseCase.activate(categoryId);
        return toPayload(getCategoryByIdUseCase.execute(CategoryId.of(categoryId)));
    }

    // ─── Nested resolvers ───

    @DgsData(parentType = "CategoryPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        CategoryPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload.getCompanyId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "CategoryPayload")
    public CompletableFuture<CategoryPayload> parentCategory(DgsDataFetchingEnvironment dfe) {
        CategoryPayload payload = dfe.getSource();
        DataLoader<String, CategoryPayload> dataLoader = dfe.getDataLoader("categoryLoader");
        if (dataLoader == null || payload.getParentCategoryId() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return dataLoader.load(payload.getParentCategoryId());
    }

    @DgsData(parentType = "CategoryPayload")
    public List<CategoryPayload> subCategories(DgsDataFetchingEnvironment dfe) {
        CategoryPayload payload = dfe.getSource();
        if (payload == null || payload.getId() == null) return List.of();
        return getSubCategoriesUseCase.getSubCategories(CategoryId.of(payload.getId()))
                .stream()
                .map(CategoryGraphql::toPayload)
                .toList();
    }
}
