package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.hr.validators.CreatePerformanceReviewInputValidator;

@Data
@Validate(validator = CreatePerformanceReviewInputValidator.class)
public class CreatePerformanceReviewInput implements Validatable {
    private String companyId;
    private String cycleId;
    private String employeeId;
    private String reviewerId;
}
