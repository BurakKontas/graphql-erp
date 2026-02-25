package tr.kontas.erp.app.reference.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.reference.validators.CreateCurrencyInputValidator;

@Data
@Validate(validator = CreateCurrencyInputValidator.class)
public class CreateCurrencyInput implements Validatable {
    private String code;
    private String name;
    private String symbol;
    private int fractionDigits;
}
