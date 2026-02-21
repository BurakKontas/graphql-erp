package tr.kontas.erp.inventory.domain.warehouse;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class WarehouseId extends Identifier {

    private WarehouseId(UUID value) {
        super(value);
    }

    public static WarehouseId newId() {
        return new WarehouseId(UUID.randomUUID());
    }

    public static WarehouseId of(UUID value) {
        return new WarehouseId(value);
    }

    public static WarehouseId of(String value) {
        return new WarehouseId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
