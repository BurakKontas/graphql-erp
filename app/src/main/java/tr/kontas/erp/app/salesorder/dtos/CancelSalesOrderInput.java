package tr.kontas.erp.app.salesorder.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.salesorder.validators.CancelSalesOrderInputValidator;

@Data
@Validate(validator = CancelSalesOrderInputValidator.class)
public class CancelSalesOrderInput implements Validatable {
    private String orderId;
    private String reason;
}
