package tr.kontas.erp.finance.domain.accountingperiod;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class AccountingPeriodId extends Identifier {

    private AccountingPeriodId(UUID value) {
        super(value);
    }

    public static AccountingPeriodId newId() {
        return new AccountingPeriodId(UUID.randomUUID());
    }

    public static AccountingPeriodId of(UUID value) {
        return new AccountingPeriodId(value);
    }

    public static AccountingPeriodId of(String value) {
        return new AccountingPeriodId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

