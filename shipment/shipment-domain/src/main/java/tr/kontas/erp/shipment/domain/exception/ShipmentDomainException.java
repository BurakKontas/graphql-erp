package tr.kontas.erp.shipment.domain.exception;

import tr.kontas.erp.core.kernel.exception.DomainException;

public abstract class ShipmentDomainException extends DomainException {

    protected ShipmentDomainException(String message) {
        super(message);
    }
}

