package tr.kontas.erp.app.salesorder.validators;

import tr.kontas.erp.app.salesorder.dtos.CancelSalesOrderInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CancelSalesOrderInputValidator extends Validator<CancelSalesOrderInput> {
    public CancelSalesOrderInputValidator() {
        ruleFor(CancelSalesOrderInput::getOrderId).notNull().notBlank();
    }
}
