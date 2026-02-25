package tr.kontas.erp.app.salesorder.validators;

import tr.kontas.erp.app.salesorder.dtos.UpdateSalesOrderLineInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class UpdateSalesOrderLineInputValidator extends Validator<UpdateSalesOrderLineInput> {
    public UpdateSalesOrderLineInputValidator() {
        ruleFor(UpdateSalesOrderLineInput::getOrderId).notNull().notBlank();
        ruleFor(UpdateSalesOrderLineInput::getLineId).notNull().notBlank();
    }
}
