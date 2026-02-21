package tr.kontas.erp.shipment.domain.shipmentreturn;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;

@Getter
public class ReturnReason extends ValueObject {
    private static final int MAX_LENGTH = 500;

    private final String value;

    public ReturnReason(String value) {
        Objects.requireNonNull(value, "ReturnReason cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("ReturnReason cannot be blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "ReturnReason cannot exceed %d characters".formatted(MAX_LENGTH));
        }
        this.value = value.trim();
    }
}

