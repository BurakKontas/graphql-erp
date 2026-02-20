package tr.kontas.erp.core.domain.reference.unit;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.Identifier;

@Getter
public class UnitCode extends Identifier {
    public UnitCode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Unit code cannot be null or blank");
        }
        super(value);
    }

    @Override
    public String getValue() {
        return String.valueOf(super.getValue()).toUpperCase();
    }
}
