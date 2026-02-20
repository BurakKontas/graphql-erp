package tr.kontas.erp.core.domain.reference.unit;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.Entity;

@Getter
public class Unit extends Entity<UnitCode> {

    private final String name;
    private final UnitType type;
    private boolean active;

    public Unit(UnitCode code, String name, UnitType type) {
        super(code);

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Unit name cannot be blank");
        }

        this.name = name;
        this.type = type;
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}