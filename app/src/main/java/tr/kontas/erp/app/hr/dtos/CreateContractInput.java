package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.hr.validators.CreateContractInputValidator;

@Data
@Validate(validator = CreateContractInputValidator.class)
public class CreateContractInput implements Validatable {
    private String companyId;
    private String employeeId;
    private String startDate;
    private String endDate;
    private String contractType;
    private String grossSalary;
    private String currencyCode;
    private String payrollConfigId;
}
