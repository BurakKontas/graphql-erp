package tr.kontas.erp.inventory.application.category;

import tr.kontas.erp.inventory.domain.category.Category;
import tr.kontas.erp.inventory.domain.category.CategoryId;

import java.util.List;

public interface GetCategoriesByIdsUseCase {
    List<Category> execute(List<CategoryId> ids);
}
