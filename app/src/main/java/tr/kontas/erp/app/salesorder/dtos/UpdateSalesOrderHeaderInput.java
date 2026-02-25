package tr.kontas.erp.app.salesorder.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.salesorder.validators.UpdateSalesOrderHeaderInputValidator;

@Data
@Validate(validator = UpdateSalesOrderHeaderInputValidator.class)
public class UpdateSalesOrderHeaderInput implements Validatable {
    private String orderId;
    private String orderDate;
    private String expiryDate;
    private String paymentTermCode;
    private ShippingAddressInput shippingAddress;

    @Data
    public static class ShippingAddressInput {
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String stateOrProvince;
        private String postalCode;
        private String countryCode;
    }
}
