package tr.kontas.erp.hr.platform.persistence.attendance;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.attendance.*;

public class AttendanceMapper {
    public static AttendanceJpaEntity toEntity(Attendance a) {
        AttendanceJpaEntity e = new AttendanceJpaEntity();
        e.setId(a.getId().asUUID());
        e.setTenantId(a.getTenantId().asUUID());
        e.setCompanyId(a.getCompanyId().asUUID());
        e.setEmployeeId(a.getEmployeeId());
        e.setDate(a.getDate());
        e.setSource(a.getSource().name());
        e.setCheckIn(a.getCheckIn());
        e.setCheckOut(a.getCheckOut());
        e.setStatus(a.getStatus().name());
        e.setRegularMinutes(a.getRegularMinutes());
        e.setOvertimeMinutes(a.getOvertimeMinutes());
        e.setDeviceId(a.getDeviceId());
        e.setNotes(a.getNotes());
        return e;
    }
    public static Attendance toDomain(AttendanceJpaEntity e) {
        return new Attendance(
                AttendanceId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                e.getEmployeeId(), e.getDate(), AttendanceSource.valueOf(e.getSource()),
                e.getCheckIn(), e.getCheckOut(), AttendanceStatus.valueOf(e.getStatus()),
                e.getRegularMinutes(), e.getOvertimeMinutes(), e.getDeviceId(), e.getNotes());
    }
}
