package tr.kontas.erp.hr.application.attendance;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.attendance.Attendance;

import java.util.List;

public interface GetAttendancesByCompanyUseCase {
    List<Attendance> execute(CompanyId companyId);
}
