package tr.kontas.erp.sales.domain.salesorder;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class SalesOrderId extends Identifier {

    private SalesOrderId(UUID value) {
        super(value);
    }

    public static SalesOrderId newId() {
        return new SalesOrderId(UUID.randomUUID());
    }

    public static SalesOrderId of(UUID value) {
        return new SalesOrderId(value);
    }

    public static SalesOrderId of(String value) {
        return new SalesOrderId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
