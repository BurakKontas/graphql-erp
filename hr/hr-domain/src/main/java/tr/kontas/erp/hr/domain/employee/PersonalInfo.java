package tr.kontas.erp.hr.domain.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.ValueObject;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PersonalInfo extends ValueObject {
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final String nationalId;
    private final Gender gender;
    private final String nationality;
}
