package tr.kontas.erp.hr.application.attendance;

import tr.kontas.erp.hr.domain.attendance.AttendanceId;

public interface CreateAttendanceUseCase {
    AttendanceId execute(CreateAttendanceCommand command);
}
