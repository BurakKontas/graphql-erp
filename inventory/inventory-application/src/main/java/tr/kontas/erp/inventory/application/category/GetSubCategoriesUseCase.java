package tr.kontas.erp.inventory.application.category;

import tr.kontas.erp.inventory.domain.category.Category;
import tr.kontas.erp.inventory.domain.category.CategoryId;

import java.util.List;

public interface GetSubCategoriesUseCase {
    List<Category> getSubCategories(CategoryId parentId);
}

