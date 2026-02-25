package tr.kontas.erp.app.reference.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.reference.validators.CreateTaxInputValidator;

import java.math.BigDecimal;

@Data
@Validate(validator = CreateTaxInputValidator.class)
public class CreateTaxInput implements Validatable {
    private String companyId;
    private String code;
    private String name;
    private String type;
    private BigDecimal rate;
}
