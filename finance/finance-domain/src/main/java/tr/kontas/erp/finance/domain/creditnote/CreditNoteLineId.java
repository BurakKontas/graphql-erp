package tr.kontas.erp.finance.domain.creditnote;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class CreditNoteLineId extends Identifier {

    private CreditNoteLineId(UUID value) {
        super(value);
    }

    public static CreditNoteLineId newId() {
        return new CreditNoteLineId(UUID.randomUUID());
    }

    public static CreditNoteLineId of(UUID value) {
        return new CreditNoteLineId(value);
    }

    public static CreditNoteLineId of(String value) {
        return new CreditNoteLineId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

