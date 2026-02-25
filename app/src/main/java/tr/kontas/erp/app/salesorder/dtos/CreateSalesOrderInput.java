package tr.kontas.erp.app.salesorder.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.salesorder.validators.CreateSalesOrderInputValidator;

@Data
@Validate(validator = CreateSalesOrderInputValidator.class)
public class CreateSalesOrderInput implements Validatable {
    private String companyId;
    private String customerId;
    private String currencyCode;
    private String paymentTermCode;
    private String orderDate;
}
