package tr.kontas.erp.inventory.application.category;

import tr.kontas.erp.inventory.domain.category.Category;
import tr.kontas.erp.inventory.domain.category.CategoryId;

public interface GetCategoryByIdUseCase {
    Category execute(CategoryId id);
}
