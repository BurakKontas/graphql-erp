package tr.kontas.erp.shipment.domain.shipment;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class ShipmentNumber extends ValueObject {
    private static final Pattern FORMAT = Pattern.compile("^SH-\\d{4}-\\d{6}$");

    private final String value;

    public ShipmentNumber(String value) {
        Objects.requireNonNull(value, "ShipmentNumber cannot be null");
        if (!FORMAT.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid ShipmentNumber format '%s'. Expected SH-YYYY-NNNNNN".formatted(value));
        }
        this.value = value;
    }
}

