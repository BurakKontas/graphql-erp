package tr.kontas.erp.app.businesspartner.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.businesspartner.validators.CreateBusinessPartnerInputValidator;

import java.util.Set;

@Data
@Validate(validator = CreateBusinessPartnerInputValidator.class)
public class CreateBusinessPartnerInput implements Validatable {
    private String companyId;
    private String code;
    private String name;
    private Set<String> roles;
    private String taxNumber; // nullable
}
