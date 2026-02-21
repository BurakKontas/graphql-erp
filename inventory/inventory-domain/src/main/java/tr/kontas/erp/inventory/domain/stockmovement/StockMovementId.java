package tr.kontas.erp.inventory.domain.stockmovement;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class StockMovementId extends Identifier {

    private StockMovementId(UUID value) {
        super(value);
    }

    public static StockMovementId newId() {
        return new StockMovementId(UUID.randomUUID());
    }

    public static StockMovementId of(UUID value) {
        return new StockMovementId(value);
    }

    public static StockMovementId of(String value) {
        return new StockMovementId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
