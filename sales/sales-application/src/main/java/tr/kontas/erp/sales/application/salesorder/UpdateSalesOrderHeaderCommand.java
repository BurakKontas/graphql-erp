package tr.kontas.erp.sales.application.salesorder;

import java.time.LocalDate;

public record UpdateSalesOrderHeaderCommand(
        String orderId,
        LocalDate orderDate,
        LocalDate expiryDate,
        String paymentTermCode,
        ShippingAddressInput shippingAddress
) {

    public record ShippingAddressInput(
            String addressLine1,
            String addressLine2,
            String city,
            String stateOrProvince,
            String postalCode,
            String countryCode
    ) {
    }
}
