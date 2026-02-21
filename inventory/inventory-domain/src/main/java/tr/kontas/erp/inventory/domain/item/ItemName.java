package tr.kontas.erp.inventory.domain.item;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;

@Getter
public class ItemName extends ValueObject {
    private static final int MAX_LENGTH = 200;

    private final String value;

    public ItemName(String value) {
        Objects.requireNonNull(value, "ItemName cannot be null");
        if (value.isBlank())
            throw new IllegalArgumentException("ItemName cannot be blank");
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException(
                    "ItemName cannot exceed %d characters".formatted(MAX_LENGTH));
        this.value = value.trim().toUpperCase();
    }
}
