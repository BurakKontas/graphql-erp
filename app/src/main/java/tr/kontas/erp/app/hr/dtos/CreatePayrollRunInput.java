package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.hr.validators.CreatePayrollRunInputValidator;

@Data
@Validate(validator = CreatePayrollRunInputValidator.class)
public class CreatePayrollRunInput implements Validatable {
    private String companyId;
    private int year;
    private int month;
    private String paymentDate;
    private String payrollConfigId;
}
