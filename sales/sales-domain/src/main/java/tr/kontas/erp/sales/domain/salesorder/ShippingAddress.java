package tr.kontas.erp.sales.domain.salesorder;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.util.Objects;

@Getter
@Builder
@ToString(callSuper = false)
public class ShippingAddress extends ValueObject {
    private String addressLine1;
    private String addressLine2;      // nullable
    private String city;
    private String stateOrProvince;   // nullable
    private String postalCode;
    private String countryCode;       // ISO 3166-1 alpha-2

    public ShippingAddress(
            String addressLine1,
            String addressLine2,
            String city,
            String stateOrProvince,
            String postalCode,
            String countryCode
    ) {
        this.addressLine1 = requireNonBlank(addressLine1, "addressLine1");
        this.addressLine2 = addressLine2;
        this.city = requireNonBlank(city, "city");
        this.stateOrProvince = stateOrProvince;
        this.postalCode = requireNonBlank(postalCode, "postalCode");
        this.countryCode = validateCountryCode(countryCode);
    }

    private static String requireNonBlank(String value, String field) {
        Objects.requireNonNull(value, field + " is required");
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value;
    }

    private static String validateCountryCode(String code) {
        Objects.requireNonNull(code, "countryCode is required");
        if (!code.matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException(
                    "countryCode must be ISO 3166-1 alpha-2 (2 uppercase letters), got: " + code);
        }
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShippingAddress that)) return false;
        return Objects.equals(addressLine1, that.addressLine1)
                && Objects.equals(addressLine2, that.addressLine2)
                && Objects.equals(city, that.city)
                && Objects.equals(stateOrProvince, that.stateOrProvince)
                && Objects.equals(postalCode, that.postalCode)
                && Objects.equals(countryCode, that.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressLine1, addressLine2, city, stateOrProvince, postalCode, countryCode);
    }
}