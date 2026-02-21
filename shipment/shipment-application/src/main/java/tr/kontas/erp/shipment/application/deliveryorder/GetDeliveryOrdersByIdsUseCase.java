package tr.kontas.erp.shipment.application.deliveryorder;

import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrder;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderId;

import java.util.List;

public interface GetDeliveryOrdersByIdsUseCase {
    List<DeliveryOrder> execute(List<DeliveryOrderId> ids);
}

