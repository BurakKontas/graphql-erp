package tr.kontas.erp.app.hr.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.hr.validators.CreatePayrollConfigInputValidator;

@Data
@Validate(validator = CreatePayrollConfigInputValidator.class)
public class CreatePayrollConfigInput implements Validatable {
    private String companyId;
    private String countryCode;
    private String name;
    private int validYear;
    private String minimumWage;
    private String currencyCode;
}
