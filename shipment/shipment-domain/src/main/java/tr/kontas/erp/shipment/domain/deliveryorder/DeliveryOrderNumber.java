package tr.kontas.erp.shipment.domain.deliveryorder;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class DeliveryOrderNumber extends ValueObject {
    private static final Pattern FORMAT = Pattern.compile("^DO-\\d{4}-\\d{6}$");

    private final String value;

    public DeliveryOrderNumber(String value) {
        Objects.requireNonNull(value, "DeliveryOrderNumber cannot be null");
        if (!FORMAT.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid DeliveryOrderNumber format '%s'. Expected DO-YYYY-NNNNNN".formatted(value));
        }
        this.value = value;
    }
}

