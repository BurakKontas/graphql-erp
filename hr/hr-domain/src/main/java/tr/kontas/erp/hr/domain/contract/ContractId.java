package tr.kontas.erp.hr.domain.contract;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class ContractId extends Identifier {
    private ContractId(UUID value) {
        super(value);
    }

    public static ContractId newId() {
        return new ContractId(UUID.randomUUID());
    }

    public static ContractId of(UUID value) {
        return new ContractId(value);
    }

    public static ContractId of(String value) {
        return new ContractId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
