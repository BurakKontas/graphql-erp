package tr.kontas.erp.app.salesorder.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShippingAddressPayload {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateOrProvince;
    private String postalCode;
    private String countryCode;
}
