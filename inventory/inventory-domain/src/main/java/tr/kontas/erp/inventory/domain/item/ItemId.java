package tr.kontas.erp.inventory.domain.item;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class ItemId extends Identifier {

    private ItemId(UUID value) {
        super(value);
    }

    public static ItemId newId() {
        return new ItemId(UUID.randomUUID());
    }

    public static ItemId of(UUID value) {
        return new ItemId(value);
    }

    public static ItemId of(String value) {
        return new ItemId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
