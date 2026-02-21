package tr.kontas.erp.finance.domain.salesinvoice;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class InvoiceNumber extends ValueObject {
    private static final Pattern FORMAT = Pattern.compile("^INV-\\d{4}-\\d{6}$");

    private final String value;

    public InvoiceNumber(String value) {
        Objects.requireNonNull(value, "InvoiceNumber cannot be null");
        if (!FORMAT.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid InvoiceNumber format '%s'. Expected INV-YYYY-NNNNNN".formatted(value));
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

