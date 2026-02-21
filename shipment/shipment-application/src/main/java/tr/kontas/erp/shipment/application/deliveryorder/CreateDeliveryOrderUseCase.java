package tr.kontas.erp.shipment.application.deliveryorder;

import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderId;

public interface CreateDeliveryOrderUseCase {
    DeliveryOrderId execute(CreateDeliveryOrderCommand command);
}

