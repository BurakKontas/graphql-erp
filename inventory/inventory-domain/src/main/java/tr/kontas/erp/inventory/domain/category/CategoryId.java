package tr.kontas.erp.inventory.domain.category;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class CategoryId extends Identifier {

    private CategoryId(UUID value) {
        super(value);
    }

    public static CategoryId newId() {
        return new CategoryId(UUID.randomUUID());
    }

    public static CategoryId of(UUID value) {
        return new CategoryId(value);
    }

    public static CategoryId of(String value) {
        return new CategoryId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
