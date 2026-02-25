package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.hr.validators.CreatePerformanceCycleInputValidator;

@Data
@Validate(validator = CreatePerformanceCycleInputValidator.class)
public class CreatePerformanceCycleInput implements Validatable {
    private String companyId;
    private String name;
    private String startDate;
    private String endDate;
    private String reviewDeadline;
}
