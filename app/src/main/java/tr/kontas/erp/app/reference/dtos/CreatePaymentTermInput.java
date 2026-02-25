package tr.kontas.erp.app.reference.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.reference.validators.CreatePaymentTermInputValidator;

@Data
@Validate(validator = CreatePaymentTermInputValidator.class)
public class CreatePaymentTermInput implements Validatable {
    private String companyId;
    private String code;
    private String name;
    private int dueDays;
}
