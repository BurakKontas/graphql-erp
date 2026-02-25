package tr.kontas.erp.app.hr.validators;

import tr.kontas.erp.app.hr.dtos.CreatePerformanceReviewInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreatePerformanceReviewInputValidator extends Validator<CreatePerformanceReviewInput> {
    public CreatePerformanceReviewInputValidator() {
        ruleFor(CreatePerformanceReviewInput::getCompanyId).notNull().notBlank();
        ruleFor(CreatePerformanceReviewInput::getEmployeeId).notNull().notBlank();
        ruleFor(CreatePerformanceReviewInput::getCycleId).notNull().notBlank();
    }
}
