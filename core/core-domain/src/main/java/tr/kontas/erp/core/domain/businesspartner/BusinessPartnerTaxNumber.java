package tr.kontas.erp.core.domain.businesspartner;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class BusinessPartnerTaxNumber extends ValueObject {
    private final String value;

    public BusinessPartnerTaxNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("BusinessPartner taxnumber cannot be empty");
        }
        this.value = value;
    }

}
