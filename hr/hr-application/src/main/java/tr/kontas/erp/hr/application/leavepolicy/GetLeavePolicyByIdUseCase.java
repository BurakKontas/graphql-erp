package tr.kontas.erp.hr.application.leavepolicy;

import tr.kontas.erp.hr.domain.leavepolicy.LeavePolicy;
import tr.kontas.erp.hr.domain.leavepolicy.LeavePolicyId;

public interface GetLeavePolicyByIdUseCase {
    LeavePolicy execute(LeavePolicyId id);
}
