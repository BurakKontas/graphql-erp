package tr.kontas.erp.hr.domain.leaverequest;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class LeaveRequestId extends Identifier {
    private LeaveRequestId(UUID value) {
        super(value);
    }

    public static LeaveRequestId newId() {
        return new LeaveRequestId(UUID.randomUUID());
    }

    public static LeaveRequestId of(UUID value) {
        return new LeaveRequestId(value);
    }

    public static LeaveRequestId of(String value) {
        return new LeaveRequestId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
