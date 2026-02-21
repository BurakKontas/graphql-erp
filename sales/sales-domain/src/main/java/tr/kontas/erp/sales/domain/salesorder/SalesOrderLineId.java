package tr.kontas.erp.sales.domain.salesorder;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class SalesOrderLineId extends Identifier {

    private SalesOrderLineId(UUID value) {
        super(value);
    }

    public static SalesOrderLineId newId() {
        return new SalesOrderLineId(UUID.randomUUID());
    }

    public static SalesOrderLineId of(UUID value) {
        return new SalesOrderLineId(value);
    }

    public static SalesOrderLineId of(String value) {
        return new SalesOrderLineId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
