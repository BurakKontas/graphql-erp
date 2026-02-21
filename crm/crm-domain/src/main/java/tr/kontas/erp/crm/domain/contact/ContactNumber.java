package tr.kontas.erp.crm.domain.contact;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
public class ContactNumber extends ValueObject {

    private final String value;

    public ContactNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Contact number cannot be blank");
        }
        this.value = value;
    }
}
