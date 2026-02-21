package tr.kontas.erp.hr.domain.leavebalance;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class LeaveBalanceId extends Identifier {
    private LeaveBalanceId(UUID value) {
        super(value);
    }

    public static LeaveBalanceId newId() {
        return new LeaveBalanceId(UUID.randomUUID());
    }

    public static LeaveBalanceId of(UUID value) {
        return new LeaveBalanceId(value);
    }

    public static LeaveBalanceId of(String value) {
        return new LeaveBalanceId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
