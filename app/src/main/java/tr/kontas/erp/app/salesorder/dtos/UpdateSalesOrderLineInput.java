package tr.kontas.erp.app.salesorder.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.salesorder.validators.UpdateSalesOrderLineInputValidator;

import java.math.BigDecimal;

@Data
@Validate(validator = UpdateSalesOrderLineInputValidator.class)
public class UpdateSalesOrderLineInput implements Validatable {
    private String orderId;
    private String lineId;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
}
