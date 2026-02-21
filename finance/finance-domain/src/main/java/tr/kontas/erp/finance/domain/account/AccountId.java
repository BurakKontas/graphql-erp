package tr.kontas.erp.finance.domain.account;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class AccountId extends Identifier {

    private AccountId(UUID value) {
        super(value);
    }

    public static AccountId newId() {
        return new AccountId(UUID.randomUUID());
    }

    public static AccountId of(UUID value) {
        return new AccountId(value);
    }

    public static AccountId of(String value) {
        return new AccountId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

