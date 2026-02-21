package tr.kontas.erp.crm.domain.quote;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class QuoteId extends Identifier {

    private QuoteId(UUID value) {
        super(value);
    }


    public static QuoteId newId() {
        return new QuoteId(UUID.randomUUID());
    }


    public static QuoteId of(UUID value) {
        return new QuoteId(value);
    }


    public static QuoteId of(String value) {
        return new QuoteId(UUID.fromString(value));
    }


    public UUID asUUID() {
        return (UUID) getValue();
    }
}

