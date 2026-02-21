package tr.kontas.erp.hr.domain.position;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class PositionCode extends ValueObject {

    private final String value;

    public PositionCode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PositionCode cannot be empty");
        }
        this.value = value;
    }
}
