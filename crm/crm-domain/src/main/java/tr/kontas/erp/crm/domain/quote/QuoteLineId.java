package tr.kontas.erp.crm.domain.quote;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class QuoteLineId extends Identifier {

    private QuoteLineId(UUID value) {
        super(value);
    }


    public static QuoteLineId newId() {
        return new QuoteLineId(UUID.randomUUID());
    }


    public static QuoteLineId of(UUID value) {
        return new QuoteLineId(value);
    }


    public static QuoteLineId of(String value) {
        return new QuoteLineId(UUID.fromString(value));
    }


    public UUID asUUID() {
        return (UUID) getValue();
    }
}

