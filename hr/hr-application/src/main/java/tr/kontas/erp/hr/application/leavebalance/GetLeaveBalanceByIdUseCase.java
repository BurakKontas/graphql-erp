package tr.kontas.erp.hr.application.leavebalance;

import tr.kontas.erp.hr.domain.leavebalance.LeaveBalance;
import tr.kontas.erp.hr.domain.leavebalance.LeaveBalanceId;

public interface GetLeaveBalanceByIdUseCase {
    LeaveBalance execute(LeaveBalanceId id);
}
