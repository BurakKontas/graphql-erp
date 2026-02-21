package tr.kontas.erp.shipment.application.shipment;

public interface SetTrackingInfoUseCase {
    void execute(String shipmentId, String carrierName, String trackingNumber);
}

