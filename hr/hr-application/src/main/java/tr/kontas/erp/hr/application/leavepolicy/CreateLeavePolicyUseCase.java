package tr.kontas.erp.hr.application.leavepolicy;

import tr.kontas.erp.hr.domain.leavepolicy.LeavePolicyId;

public interface CreateLeavePolicyUseCase {
    LeavePolicyId execute(CreateLeavePolicyCommand command);
}
