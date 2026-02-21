package tr.kontas.erp.inventory.application.category;

import tr.kontas.erp.inventory.domain.category.CategoryId;

public interface CreateCategoryUseCase {
    CategoryId execute(CreateCategoryCommand command);
}
