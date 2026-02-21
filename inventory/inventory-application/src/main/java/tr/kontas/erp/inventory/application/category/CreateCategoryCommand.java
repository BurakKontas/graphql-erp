package tr.kontas.erp.inventory.application.category;

import tr.kontas.erp.core.domain.company.CompanyId;

public record CreateCategoryCommand(
        CompanyId companyId,
        String name,
        String parentCategoryId
) {
}
