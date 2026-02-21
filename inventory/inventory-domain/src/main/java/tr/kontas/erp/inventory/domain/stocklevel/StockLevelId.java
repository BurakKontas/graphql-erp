package tr.kontas.erp.inventory.domain.stocklevel;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class StockLevelId extends Identifier {

    private StockLevelId(UUID value) {
        super(value);
    }

    public static StockLevelId newId() {
        return new StockLevelId(UUID.randomUUID());
    }

    public static StockLevelId of(UUID value) {
        return new StockLevelId(value);
    }

    public static StockLevelId of(String value) {
        return new StockLevelId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
