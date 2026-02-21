package tr.kontas.erp.inventory.domain.warehouse;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;

@Getter
public class WarehouseCode extends ValueObject {
    private static final int MAX_LENGTH = 20;

    private final String value;

    public WarehouseCode(String value) {
        Objects.requireNonNull(value, "WarehouseCode cannot be null");
        if (value.isBlank())
            throw new IllegalArgumentException("WarehouseCode cannot be blank");
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException(
                    "WarehouseCode cannot exceed %d characters".formatted(MAX_LENGTH));
        this.value = value.trim().toUpperCase();
    }
}
