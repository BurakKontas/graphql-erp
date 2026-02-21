package tr.kontas.erp.inventory.application.category;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.inventory.domain.category.Category;

import java.util.List;

public interface GetCategoriesByCompanyUseCase {
    List<Category> execute(CompanyId companyId);
}
