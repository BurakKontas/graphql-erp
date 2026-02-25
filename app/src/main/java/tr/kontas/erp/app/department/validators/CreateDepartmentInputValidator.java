package tr.kontas.erp.app.department.validators;

import tr.kontas.fluentvalidation.validation.Validator;
import tr.kontas.erp.app.department.dtos.CreateDepartmentInput;

public class CreateDepartmentInputValidator extends Validator<CreateDepartmentInput> {
    public CreateDepartmentInputValidator() {
        ruleFor(CreateDepartmentInput::getName)
                .notNull()
                .notBlank()
                .withMessage("Department name is required");

        ruleFor(CreateDepartmentInput::getCompanyId)
                .notNull()
                .notBlank()
                .withMessage("companyId is required");

        ruleFor(CreateDepartmentInput::getParentId)
                .unless(input -> input.getParentId() == null)
                .isUuid()
                .notBlank();
    }
}
