package tr.kontas.erp.app.salesorder.validators;

import tr.kontas.erp.app.salesorder.dtos.CreateSalesOrderInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateSalesOrderInputValidator extends Validator<CreateSalesOrderInput> {
    public CreateSalesOrderInputValidator() {
        ruleFor(CreateSalesOrderInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateSalesOrderInput::getCustomerId).notNull().notBlank();
        ruleFor(CreateSalesOrderInput::getOrderDate).notNull().notBlank();
    }
}
