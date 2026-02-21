package tr.kontas.erp.inventory.application.item;

import tr.kontas.erp.core.domain.company.CompanyId;

public record CreateItemCommand(
        CompanyId companyId,
        String code,
        String name,
        String type,
        String unitCode,
        String categoryId,
        boolean allowNegativeStock
) {
}
