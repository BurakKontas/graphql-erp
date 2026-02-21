package tr.kontas.erp.inventory.application.warehouse;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.inventory.domain.warehouse.Warehouse;

import java.util.List;

public interface GetWarehousesByCompanyUseCase {
    List<Warehouse> execute(CompanyId companyId);
}
