package tr.kontas.erp.app.finance.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.finance.validators.CreateAccountingPeriodInputValidator;

@Data
@Validate(validator = CreateAccountingPeriodInputValidator.class)
public class CreateAccountingPeriodInput implements Validatable {
    private String companyId;
    private String periodType;
    private String startDate;
    private String endDate;
}
