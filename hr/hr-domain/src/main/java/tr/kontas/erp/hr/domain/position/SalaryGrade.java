package tr.kontas.erp.hr.domain.position;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class SalaryGrade extends ValueObject {

    private final String value;

    public SalaryGrade(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SalaryGrade cannot be empty");
        }
        this.value = value;
    }
}
