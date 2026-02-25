package tr.kontas.erp.app.salesorder.validators;

import tr.kontas.erp.app.salesorder.dtos.AddSalesOrderLineInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class AddSalesOrderLineInputValidator extends Validator<AddSalesOrderLineInput> {
    public AddSalesOrderLineInputValidator() {
        ruleFor(AddSalesOrderLineInput::getOrderId).notNull().notBlank();
        ruleFor(AddSalesOrderLineInput::getItemId).notNull().notBlank();
        ruleFor(AddSalesOrderLineInput::getQuantity).notNull();
    }
}
