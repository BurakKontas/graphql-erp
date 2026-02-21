package tr.kontas.erp.hr.application.leavebalance;

import tr.kontas.erp.hr.domain.leavebalance.LeaveBalanceId;

public interface CreateLeaveBalanceUseCase {
    LeaveBalanceId execute(CreateLeaveBalanceCommand command);
}
