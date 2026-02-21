package tr.kontas.erp.core.domain.shared;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public final class Email extends ValueObject {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^(?:(?![0-9])[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
                    "|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]" +
                    "|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                    "@" +
                    "(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?" +
                    "|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}" +
                    "(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])" +
                    "|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]" +
                    "|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$"
    );

    private final String value;

    public Email(String value) {
        Objects.requireNonNull(value, "Email cannot be null");

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!isValidEmail(trimmed)) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }

        // Normalize to lowercase for consistency
        this.value = trimmed.toLowerCase();
    }

    public static Email of(String value) {
        return new Email(value);
    }

    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }

    public String getLocalPart() {
        return value.substring(0, value.indexOf('@'));
    }

    public boolean isFromDomain(String domain) {
        Objects.requireNonNull(domain, "Domain cannot be null");
        return this.getDomain().equalsIgnoreCase(domain.trim());
    }

    public boolean isFromDomain(Domain domain) {
        Objects.requireNonNull(domain, "Domain cannot be null");
        return this.getDomain().equalsIgnoreCase(domain.value());
    }

    public boolean isCorporate() {
        String domain = getDomain().toLowerCase();
        return !domain.equals("gmail.com") &&
                !domain.equals("yahoo.com") &&
                !domain.equals("hotmail.com") &&
                !domain.equals("outlook.com") &&
                !domain.endsWith(".edu.tr") &&
                !domain.endsWith(".edu");
    }

    public Email withDifferentLocalPart(String newLocalPart) {
        return new Email(newLocalPart + "@" + getDomain());
    }

    public Email withDifferentDomain(String newDomain) {
        return new Email(getLocalPart() + "@" + newDomain);
    }

    public String value() {
        return value;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.length() > 254) return false;

        // Check for consecutive dots
        if (email.contains("..")) return false;

        // Check for valid characters and format using regex
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email that)) return false;
        // Case-insensitive comparison (already stored as lowercase)
        return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

    // Inner class for domain-specific operations
        public record Domain(String value) {
            public Domain(String value) {
                Objects.requireNonNull(value, "Domain cannot be null");
                String trimmed = value.trim().toLowerCase();
                if (trimmed.isEmpty()) {
                    throw new IllegalArgumentException("Domain cannot be empty");
                }
                // Basic domain validation
                if (!trimmed.matches("^[a-zA-Z0-9][a-zA-Z0-9.-]*\\.[a-zA-Z]{2,}$")) {
                    throw new IllegalArgumentException("Invalid domain format: " + value);
                }
                this.value = trimmed;
            }

            public static Domain of(String value) {
                return new Domain(value);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Domain domain)) return false;
                return value.equals(domain.value);
            }

        @Override
            public String toString() {
                return value;
            }
        }
}