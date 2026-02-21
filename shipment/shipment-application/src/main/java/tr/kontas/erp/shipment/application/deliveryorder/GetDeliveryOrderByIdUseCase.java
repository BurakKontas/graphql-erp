package tr.kontas.erp.shipment.application.deliveryorder;

import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrder;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderId;

public interface GetDeliveryOrderByIdUseCase {
    DeliveryOrder execute(DeliveryOrderId id);
}

