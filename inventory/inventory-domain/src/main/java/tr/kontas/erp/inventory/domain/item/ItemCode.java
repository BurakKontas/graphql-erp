package tr.kontas.erp.inventory.domain.item;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;

@Getter
public class ItemCode extends ValueObject {
    private static final int MAX_LENGTH = 50;

    private final String value;

    public ItemCode(String value) {
        Objects.requireNonNull(value, "ItemCode cannot be null");
        if (value.isBlank())
            throw new IllegalArgumentException("ItemCode cannot be blank");
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException(
                    "ItemCode cannot exceed %d characters".formatted(MAX_LENGTH));
        this.value = value.trim().toUpperCase();
    }
}
