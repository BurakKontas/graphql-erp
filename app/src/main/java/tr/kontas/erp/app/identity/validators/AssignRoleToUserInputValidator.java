package tr.kontas.erp.app.identity.validators;

import tr.kontas.erp.app.identity.dtos.AssignRoleToUserInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class AssignRoleToUserInputValidator extends Validator<AssignRoleToUserInput> {
    public AssignRoleToUserInputValidator() {
        ruleFor(AssignRoleToUserInput::getUserId).notNull().notBlank();
        ruleFor(AssignRoleToUserInput::getRoleId).notNull().notBlank();
    }
}
