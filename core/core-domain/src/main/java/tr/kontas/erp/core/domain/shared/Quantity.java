package tr.kontas.erp.core.domain.shared;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
public final class Quantity {

    private static final int SCALE = 4;
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private final BigDecimal value;

    public Quantity(BigDecimal value) {
        Objects.requireNonNull(value, "Quantity value cannot be null");
        if (value.compareTo(ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be positive, got: " + value.toPlainString());
        }
        this.value = value.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static Quantity of(BigDecimal value) {
        return new Quantity(value);
    }

    public static Quantity of(String value) {
        try {
            return new Quantity(new BigDecimal(value));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Quantity value: " + value, e);
        }
    }

    public static Quantity of(int value) {
        return new Quantity(BigDecimal.valueOf(value));
    }

    public Quantity add(Quantity other) {
        Objects.requireNonNull(other, "Cannot add null Quantity");
        return new Quantity(this.value.add(other.value));
    }

    public Quantity subtract(Quantity other) {
        Objects.requireNonNull(other, "Cannot subtract null Quantity");
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity subtraction would result in non-positive value");
        }
        return new Quantity(result);
    }

    public boolean isGreaterThan(Quantity other) {
        return this.value.compareTo(other.value) > 0;
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity that)) return false;
        // compareTo used intentionally — 1.0000 == 1 for quantity purposes
        return this.value.compareTo(that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.stripTrailingZeros());
    }

    @Override
    public String toString() {
        return value.stripTrailingZeros().toPlainString();
    }
}