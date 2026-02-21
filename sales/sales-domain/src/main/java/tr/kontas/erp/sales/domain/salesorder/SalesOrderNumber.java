package tr.kontas.erp.sales.domain.salesorder;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class SalesOrderNumber extends ValueObject {
    private static final Pattern FORMAT = Pattern.compile("^SO-\\d{4}-\\d{6}$");

    private final String value;

    public SalesOrderNumber(String value) {
        Objects.requireNonNull(value, "SalesOrderNumber cannot be null");
        if (!FORMAT.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid SalesOrderNumber format '%s'. Expected SO-YYYY-NNNNNN".formatted(value));
        }
        this.value = value;
    }

}
