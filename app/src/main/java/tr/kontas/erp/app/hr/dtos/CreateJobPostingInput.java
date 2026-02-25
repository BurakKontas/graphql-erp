package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.hr.validators.CreateJobPostingInputValidator;

@Data
@Validate(validator = CreateJobPostingInputValidator.class)
public class CreateJobPostingInput implements Validatable {
    private String companyId;
    private String positionId;
    private String title;
    private String description;
    private String employmentType;
    private String closingDate;
}
