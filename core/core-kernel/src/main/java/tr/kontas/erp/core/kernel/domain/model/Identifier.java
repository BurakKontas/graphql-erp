package tr.kontas.erp.core.kernel.domain.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public abstract class Identifier implements Serializable {

    private final Object value;

    protected Identifier(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Identifier value cannot be null");
        }
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}