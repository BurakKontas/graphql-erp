package tr.kontas.erp.inventory.application.warehouse;

import tr.kontas.erp.core.domain.company.CompanyId;

public record CreateWarehouseCommand(
        CompanyId companyId,
        String code,
        String name
) {
}
