package tr.kontas.erp.crm.domain.opportunity;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class OpportunityNumber extends ValueObject {

    private final String value;

    public OpportunityNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Opportunity number cannot be blank");
        }
        this.value = value;
    }
}
