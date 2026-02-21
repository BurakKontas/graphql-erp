package tr.kontas.erp.inventory.domain.category;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class CategoryName extends ValueObject {
    private static final int MAX_LENGTH = 200;

    private final String value;

    public CategoryName(String value) {
        Objects.requireNonNull(value, "CategoryName cannot be null");
        if (value.isBlank())
            throw new IllegalArgumentException("CategoryName cannot be blank");
        if (value.length() > MAX_LENGTH)
            throw new IllegalArgumentException(
                    "CategoryName cannot exceed %d characters".formatted(MAX_LENGTH));
        this.value = value.trim();
    }
}
