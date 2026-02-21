package tr.kontas.erp.finance.domain.journalentry;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class JournalEntryId extends Identifier {

    private JournalEntryId(UUID value) {
        super(value);
    }

    public static JournalEntryId newId() {
        return new JournalEntryId(UUID.randomUUID());
    }

    public static JournalEntryId of(UUID value) {
        return new JournalEntryId(value);
    }

    public static JournalEntryId of(String value) {
        return new JournalEntryId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

