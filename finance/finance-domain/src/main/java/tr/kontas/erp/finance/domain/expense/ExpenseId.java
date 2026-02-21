package tr.kontas.erp.finance.domain.expense;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class ExpenseId extends Identifier {

    private ExpenseId(UUID value) {
        super(value);
    }

    public static ExpenseId newId() {
        return new ExpenseId(UUID.randomUUID());
    }

    public static ExpenseId of(UUID value) {
        return new ExpenseId(value);
    }

    public static ExpenseId of(String value) {
        return new ExpenseId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

