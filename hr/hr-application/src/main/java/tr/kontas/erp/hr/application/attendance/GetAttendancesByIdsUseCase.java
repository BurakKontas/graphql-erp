package tr.kontas.erp.hr.application.attendance;

import tr.kontas.erp.hr.domain.attendance.Attendance;
import tr.kontas.erp.hr.domain.attendance.AttendanceId;

import java.util.List;

public interface GetAttendancesByIdsUseCase {
    List<Attendance> execute(List<AttendanceId> ids);
}
