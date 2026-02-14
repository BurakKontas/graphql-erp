package tr.kontas.erp.core.kernel.domain.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class Entity<ID extends Identifier> {
    protected ID id;

    protected Entity(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Entity id cannot be null");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity<?> entity)) return false;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
