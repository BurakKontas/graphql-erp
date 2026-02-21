package tr.kontas.erp.crm.domain.lead;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class LeadId extends Identifier {

    private LeadId(UUID value) {
        super(value);
    }


    public static LeadId newId() {
        return new LeadId(UUID.randomUUID());
    }


    public static LeadId of(UUID value) {
        return new LeadId(value);
    }


    public static LeadId of(String value) {
        return new LeadId(UUID.fromString(value));
    }


    public UUID asUUID() {
        return (UUID) getValue();
    }
}

