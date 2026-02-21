package tr.kontas.erp.hr.domain.jobapplication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

@Getter
@AllArgsConstructor
public class ApplicantInfo extends ValueObject {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final String cvRef;
}
