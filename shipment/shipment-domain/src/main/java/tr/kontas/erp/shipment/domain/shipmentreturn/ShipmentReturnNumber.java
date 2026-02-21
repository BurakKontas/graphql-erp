package tr.kontas.erp.shipment.domain.shipmentreturn;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class ShipmentReturnNumber extends ValueObject {
    private static final Pattern FORMAT = Pattern.compile("^SR-\\d{4}-\\d{6}$");

    private final String value;

    public ShipmentReturnNumber(String value) {
        Objects.requireNonNull(value, "ShipmentReturnNumber cannot be null");
        if (!FORMAT.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid ShipmentReturnNumber format '%s'. Expected SR-YYYY-NNNNNN".formatted(value));
        }
        this.value = value;
    }
}

