package tr.kontas.erp.inventory.application.item;

import tr.kontas.erp.inventory.domain.item.Item;
import tr.kontas.erp.inventory.domain.item.ItemId;

public interface GetItemByIdUseCase {
    Item execute(ItemId id);
}
