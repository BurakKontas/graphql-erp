package tr.kontas.erp.core.kernel.domain.model;

import java.util.Objects;

public abstract class ValueObject {

    @Override
    public boolean equals(Object o) {
        return o != null &&
                getClass() == o.getClass() &&
                Objects.equals(this.toString(), o.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.toString());
    }

    @Override
    public String toString() {
        return getValue().toString();
    }

    public Object getValue() {
        return this;
    }
}
