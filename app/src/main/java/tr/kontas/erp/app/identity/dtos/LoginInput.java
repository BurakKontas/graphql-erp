package tr.kontas.erp.app.identity.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.kontas.erp.app.identity.validators.LoginInputValidator;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validate(validator = LoginInputValidator.class)
public class LoginInput implements Validatable {
    private String username;
    private String password;
    private String idToken;
}
