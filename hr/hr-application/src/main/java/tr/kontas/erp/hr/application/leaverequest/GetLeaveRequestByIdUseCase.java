package tr.kontas.erp.hr.application.leaverequest;

import tr.kontas.erp.hr.domain.leaverequest.LeaveRequest;
import tr.kontas.erp.hr.domain.leaverequest.LeaveRequestId;

public interface GetLeaveRequestByIdUseCase {
    LeaveRequest execute(LeaveRequestId id);
}
