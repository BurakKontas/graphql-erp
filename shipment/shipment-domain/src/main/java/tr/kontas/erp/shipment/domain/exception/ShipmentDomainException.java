package tr.kontas.erp.shipment.domain.exception;

public abstract class ShipmentDomainException extends RuntimeException {

    protected ShipmentDomainException(String message) {
        super(message);
    }

    protected ShipmentDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

