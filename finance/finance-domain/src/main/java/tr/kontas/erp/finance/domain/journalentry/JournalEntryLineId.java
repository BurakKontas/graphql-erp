package tr.kontas.erp.finance.domain.journalentry;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class JournalEntryLineId extends Identifier {

    private JournalEntryLineId(UUID value) {
        super(value);
    }

    public static JournalEntryLineId newId() {
        return new JournalEntryLineId(UUID.randomUUID());
    }

    public static JournalEntryLineId of(UUID value) {
        return new JournalEntryLineId(value);
    }

    public static JournalEntryLineId of(String value) {
        return new JournalEntryLineId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

