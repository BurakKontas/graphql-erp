package tr.kontas.erp.app.reference.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.reference.validators.UpdateTaxRateInputValidator;

import java.math.BigDecimal;

@Data
@Validate(validator = UpdateTaxRateInputValidator.class)
public class UpdateTaxRateInput implements Validatable {
    private String companyId;
    private String taxCode;
    private BigDecimal newRate;
}
