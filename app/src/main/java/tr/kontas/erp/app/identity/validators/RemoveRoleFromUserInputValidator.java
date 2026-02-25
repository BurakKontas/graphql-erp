package tr.kontas.erp.app.identity.validators;

import tr.kontas.erp.app.identity.dtos.RemoveRoleFromUserInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class RemoveRoleFromUserInputValidator extends Validator<RemoveRoleFromUserInput> {
    public RemoveRoleFromUserInputValidator() {
        ruleFor(RemoveRoleFromUserInput::getUserId).notNull().notBlank();
        ruleFor(RemoveRoleFromUserInput::getRoleId).notNull().notBlank();
    }
}
