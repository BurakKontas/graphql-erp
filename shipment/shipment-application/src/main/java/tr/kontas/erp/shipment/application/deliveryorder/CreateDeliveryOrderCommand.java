package tr.kontas.erp.shipment.application.deliveryorder;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.util.List;

public record CreateDeliveryOrderCommand(
        CompanyId companyId,
        String salesOrderId,
        String salesOrderNumber,
        String customerId,
        String addressLine1,
        String addressLine2,
        String city,
        String stateOrProvince,
        String postalCode,
        String countryCode,
        List<LineCommand> lines
) {
    public record LineCommand(
            String salesOrderLineId,
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal orderedQty
    ) {}
}

