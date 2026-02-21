package tr.kontas.erp.hr.application.leaverequest;

import tr.kontas.erp.hr.domain.leaverequest.LeaveRequest;
import tr.kontas.erp.hr.domain.leaverequest.LeaveRequestId;

import java.util.List;

public interface GetLeaveRequestsByIdsUseCase {
    List<LeaveRequest> execute(List<LeaveRequestId> ids);
}
