package tr.kontas.erp.crm.domain.lead;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class LeadNumber extends ValueObject {

    private final String value;

    public LeadNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Lead number cannot be empty");
        }
        this.value = value;
    }
}
