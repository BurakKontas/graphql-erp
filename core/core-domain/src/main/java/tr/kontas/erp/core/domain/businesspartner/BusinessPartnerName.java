package tr.kontas.erp.core.domain.businesspartner;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class BusinessPartnerName extends ValueObject {
    private final String value;

    public BusinessPartnerName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("BusinessPartner name cannot be empty");
        }
        this.value = value;
    }

}
