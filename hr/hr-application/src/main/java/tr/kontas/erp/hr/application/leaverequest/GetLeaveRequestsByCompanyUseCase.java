package tr.kontas.erp.hr.application.leaverequest;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.leaverequest.LeaveRequest;

import java.util.List;

public interface GetLeaveRequestsByCompanyUseCase {
    List<LeaveRequest> execute(CompanyId companyId);
}
