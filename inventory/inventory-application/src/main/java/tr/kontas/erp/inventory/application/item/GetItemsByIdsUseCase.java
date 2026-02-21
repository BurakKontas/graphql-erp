package tr.kontas.erp.inventory.application.item;

import tr.kontas.erp.inventory.domain.item.Item;
import tr.kontas.erp.inventory.domain.item.ItemId;

import java.util.List;

public interface GetItemsByIdsUseCase {
    List<Item> execute(List<ItemId> ids);
}
