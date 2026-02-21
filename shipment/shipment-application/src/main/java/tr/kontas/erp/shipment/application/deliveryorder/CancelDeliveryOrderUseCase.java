package tr.kontas.erp.shipment.application.deliveryorder;

public interface CancelDeliveryOrderUseCase {
    void cancel(String deliveryOrderId, String reason);
}

