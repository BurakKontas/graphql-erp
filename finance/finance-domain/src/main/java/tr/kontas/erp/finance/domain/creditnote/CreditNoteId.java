package tr.kontas.erp.finance.domain.creditnote;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class CreditNoteId extends Identifier {

    private CreditNoteId(UUID value) {
        super(value);
    }

    public static CreditNoteId newId() {
        return new CreditNoteId(UUID.randomUUID());
    }

    public static CreditNoteId of(UUID value) {
        return new CreditNoteId(value);
    }

    public static CreditNoteId of(String value) {
        return new CreditNoteId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

