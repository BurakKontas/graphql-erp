package tr.kontas.erp.shipment.application.shipmentreturn;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.util.List;

public record CreateShipmentReturnCommand(
        CompanyId companyId,
        String shipmentId,
        String salesOrderId,
        String warehouseId,
        String reason,
        List<LineCommand> lines
) {
    public record LineCommand(
            String shipmentLineId,
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal quantity,
            String lineReason
    ) {}
}

