package tr.kontas.erp.app.employee.validators;

import tr.kontas.erp.app.employee.dtos.CreateEmployeeInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateEmployeeInputValidator extends Validator<CreateEmployeeInput> {

    public CreateEmployeeInputValidator() {
        ruleFor(CreateEmployeeInput::getName)
                .notNull()
                .notBlank()
                .minLength(1)
                .withMessage("Name is required");

        ruleFor(CreateEmployeeInput::getDepartmentId)
                .notNull()
                .notBlank()
                .withMessage("departmentId is required");
    }
}
