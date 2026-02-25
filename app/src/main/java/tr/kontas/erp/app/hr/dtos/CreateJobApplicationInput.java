package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.hr.validators.CreateJobApplicationInputValidator;

@Data
@Validate(validator = CreateJobApplicationInputValidator.class)
public class CreateJobApplicationInput implements Validatable {
    private String companyId;
    private String jobPostingId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String cvRef;
}
