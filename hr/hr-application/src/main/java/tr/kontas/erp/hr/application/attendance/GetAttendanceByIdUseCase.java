package tr.kontas.erp.hr.application.attendance;

import tr.kontas.erp.hr.domain.attendance.Attendance;
import tr.kontas.erp.hr.domain.attendance.AttendanceId;

public interface GetAttendanceByIdUseCase {
    Attendance execute(AttendanceId id);
}
