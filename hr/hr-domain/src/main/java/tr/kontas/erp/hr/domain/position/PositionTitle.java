package tr.kontas.erp.hr.domain.position;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class PositionTitle extends ValueObject {

    private final String value;

    public PositionTitle(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PositionTitle cannot be empty");
        }
        this.value = value;
    }
}
