package tr.kontas.erp.hr.domain.attendance;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class AttendanceId extends Identifier {
    private AttendanceId(UUID value) {
        super(value);
    }

    public static AttendanceId newId() {
        return new AttendanceId(UUID.randomUUID());
    }

    public static AttendanceId of(UUID value) {
        return new AttendanceId(value);
    }

    public static AttendanceId of(String value) {
        return new AttendanceId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
