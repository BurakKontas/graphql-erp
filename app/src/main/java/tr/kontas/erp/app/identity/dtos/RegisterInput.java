package tr.kontas.erp.app.identity.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.identity.validators.RegisterInputValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validate(validator = RegisterInputValidator.class)
public class RegisterInput implements Validatable {
    private String username;
    private String password;
}
