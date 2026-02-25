package tr.kontas.erp.app.finance.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.finance.validators.CreateAccountInputValidator;

@Data
@Validate(validator = CreateAccountInputValidator.class)
public class CreateAccountInput implements Validatable {
    private String companyId;
    private String code;
    private String name;
    private String type;
    private String nature;
    private String parentAccountId;
    private Boolean systemAccount;
}
