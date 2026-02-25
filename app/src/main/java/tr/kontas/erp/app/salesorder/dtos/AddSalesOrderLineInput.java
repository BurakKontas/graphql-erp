package tr.kontas.erp.app.salesorder.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.salesorder.validators.AddSalesOrderLineInputValidator;

import java.math.BigDecimal;

@Data
@Validate(validator = AddSalesOrderLineInputValidator.class)
public class AddSalesOrderLineInput implements Validatable {
    private String orderId;
    private String itemId;
    private String itemDescription;
    private String unitCode;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String taxCode;
}
