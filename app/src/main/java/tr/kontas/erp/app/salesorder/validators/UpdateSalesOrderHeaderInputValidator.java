package tr.kontas.erp.app.salesorder.validators;

import tr.kontas.erp.app.salesorder.dtos.UpdateSalesOrderHeaderInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class UpdateSalesOrderHeaderInputValidator extends Validator<UpdateSalesOrderHeaderInput> {
    public UpdateSalesOrderHeaderInputValidator() {
        ruleFor(UpdateSalesOrderHeaderInput::getOrderId).notNull().notBlank();
    }
}
