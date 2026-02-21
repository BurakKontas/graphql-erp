package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.attendance.*;
import tr.kontas.erp.hr.domain.attendance.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService implements CreateAttendanceUseCase, GetAttendanceByIdUseCase,
        GetAttendancesByCompanyUseCase, GetAttendancesByIdsUseCase {

    private final AttendanceRepository attendanceRepository;

    @Override
    public AttendanceId execute(CreateAttendanceCommand cmd) {
        TenantId tenantId = TenantContext.get();
        AttendanceId id = AttendanceId.newId();
        Attendance attendance = new Attendance(id, tenantId, cmd.companyId(), cmd.employeeId(),
                cmd.date(), AttendanceSource.valueOf(cmd.source()), cmd.checkIn(), cmd.checkOut(),
                AttendanceStatus.valueOf(cmd.status()), 0, 0, cmd.deviceId(), cmd.notes());
        if (cmd.checkIn() != null && cmd.checkOut() != null) {
            attendance.recordCheckOut(cmd.checkOut());
        }
        attendanceRepository.save(attendance);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public Attendance execute(AttendanceId id) {
        return attendanceRepository.findById(id, TenantContext.get())
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendance> execute(CompanyId companyId) {
        return attendanceRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendance> execute(List<AttendanceId> ids) {
        return attendanceRepository.findByIds(ids);
    }
}
