package tr.kontas.erp.inventory.application.item;

import tr.kontas.erp.inventory.domain.item.ItemId;

public interface CreateItemUseCase {
    ItemId execute(CreateItemCommand command);
}
