package tr.kontas.erp.finance.domain.payment;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class PaymentId extends Identifier {

    private PaymentId(UUID value) {
        super(value);
    }

    public static PaymentId newId() {
        return new PaymentId(UUID.randomUUID());
    }

    public static PaymentId of(UUID value) {
        return new PaymentId(value);
    }

    public static PaymentId of(String value) {
        return new PaymentId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

