package tr.kontas.erp.sales.domain.salesorder;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;

@Getter
public class CancellationReason extends ValueObject {
    private static final int MAX_LENGTH = 500;

    private final String value;

    public CancellationReason(String value) {
        Objects.requireNonNull(value, "CancellationReason cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("CancellationReason cannot be blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "CancellationReason cannot exceed %d characters".formatted(MAX_LENGTH));
        }
        this.value = value.trim();
    }

}
