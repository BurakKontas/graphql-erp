package tr.kontas.erp.shipment.domain.exception;

import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderLineId;

import java.math.BigDecimal;

public class OvershotDeliveryException extends ShipmentDomainException {

    public OvershotDeliveryException(DeliveryOrderLineId lineId,
                                     BigDecimal orderedQty, BigDecimal attemptedShippedQty) {
        super("Overshot delivery on line '%s': orderedQty=%s, attemptedShippedQty=%s"
                .formatted(lineId, orderedQty.toPlainString(), attemptedShippedQty.toPlainString()));
    }
}

