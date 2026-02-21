package tr.kontas.erp.hr.application.leavepolicy;

import tr.kontas.erp.hr.domain.leavepolicy.LeavePolicy;
import tr.kontas.erp.hr.domain.leavepolicy.LeavePolicyId;

import java.util.List;

public interface GetLeavePolicysByIdsUseCase {
    List<LeavePolicy> execute(List<LeavePolicyId> ids);
}
