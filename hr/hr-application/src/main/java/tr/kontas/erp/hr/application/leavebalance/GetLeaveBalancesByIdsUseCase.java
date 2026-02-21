package tr.kontas.erp.hr.application.leavebalance;

import tr.kontas.erp.hr.domain.leavebalance.LeaveBalance;
import tr.kontas.erp.hr.domain.leavebalance.LeaveBalanceId;

import java.util.List;

public interface GetLeaveBalancesByIdsUseCase {
    List<LeaveBalance> execute(List<LeaveBalanceId> ids);
}
