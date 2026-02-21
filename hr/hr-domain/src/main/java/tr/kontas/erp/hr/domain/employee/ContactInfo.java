package tr.kontas.erp.hr.domain.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
@AllArgsConstructor
public class ContactInfo extends ValueObject {
    private final String personalEmail;
    private final String workEmail;
    private final String phone;
    private final String addressLine;
    private final String city;
    private final String countryCode;
}
