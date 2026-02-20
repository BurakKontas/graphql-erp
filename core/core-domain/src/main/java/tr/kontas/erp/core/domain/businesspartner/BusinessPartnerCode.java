package tr.kontas.erp.core.domain.businesspartner;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class BusinessPartnerCode extends ValueObject {
    private final String value;

    public BusinessPartnerCode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("BusinessPartner code cannot be empty");
        }
        this.value = value;
    }
}
