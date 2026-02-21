package tr.kontas.erp.crm.domain.opportunity;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class OpportunityId extends Identifier {

    private OpportunityId(UUID value) {
        super(value);
    }


    public static OpportunityId newId() {
        return new OpportunityId(UUID.randomUUID());
    }


    public static OpportunityId of(UUID value) {
        return new OpportunityId(value);
    }


    public static OpportunityId of(String value) {
        return new OpportunityId(UUID.fromString(value));
    }


    public UUID asUUID() {
        return (UUID) getValue();
    }
}

