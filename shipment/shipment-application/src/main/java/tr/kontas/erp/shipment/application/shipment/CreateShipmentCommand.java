package tr.kontas.erp.shipment.application.shipment;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.util.List;

public record CreateShipmentCommand(
        CompanyId companyId,
        String deliveryOrderId,
        String salesOrderId,
        String warehouseId,
        String addressLine1,
        String addressLine2,
        String city,
        String stateOrProvince,
        String postalCode,
        String countryCode,
        List<LineCommand> lines
) {
    public record LineCommand(
            String deliveryOrderLineId,
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal quantity
    ) {}
}

