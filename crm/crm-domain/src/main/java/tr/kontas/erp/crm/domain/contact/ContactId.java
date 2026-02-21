package tr.kontas.erp.crm.domain.contact;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class ContactId extends Identifier {

    private ContactId(UUID value) {
        super(value);
    }


    public static ContactId newId() {
        return new ContactId(UUID.randomUUID());
    }


    public static ContactId of(UUID value) {
        return new ContactId(value);
    }


    public static ContactId of(String value) {
        return new ContactId(UUID.fromString(value));
    }


    public UUID asUUID() {
        return (UUID) getValue();
    }
}

