package tr.kontas.erp.finance.domain.journalentry;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class JournalEntryNumber extends ValueObject {
    private static final Pattern FORMAT = Pattern.compile("^JE-\\d{4}-\\d{6}$");

    private final String value;

    public JournalEntryNumber(String value) {
        Objects.requireNonNull(value, "JournalEntryNumber cannot be null");
        if (!FORMAT.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid JournalEntryNumber format '%s'. Expected JE-YYYY-NNNNNN".formatted(value));
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

