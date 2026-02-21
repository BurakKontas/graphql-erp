package tr.kontas.erp.hr.application.leaverequest;

import tr.kontas.erp.hr.domain.leaverequest.LeaveRequestId;

public interface CreateLeaveRequestUseCase {
    LeaveRequestId execute(CreateLeaveRequestCommand command);
}
