package tr.kontas.erp.hr.domain.leavepolicy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaveTypeDef {
    private final LeaveType leaveType;
    private final int annualEntitlementDays;
    private final int maxCarryoverDays;
    private final boolean requiresApproval;
    private final boolean requiresDocument;
    private final int minNoticeDays;
}
