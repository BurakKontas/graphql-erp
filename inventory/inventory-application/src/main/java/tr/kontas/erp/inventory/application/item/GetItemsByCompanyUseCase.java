package tr.kontas.erp.inventory.application.item;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.inventory.domain.item.Item;

import java.util.List;

public interface GetItemsByCompanyUseCase {
    List<Item> execute(CompanyId companyId);
}
