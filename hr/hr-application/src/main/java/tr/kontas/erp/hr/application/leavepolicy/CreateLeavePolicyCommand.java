package tr.kontas.erp.hr.application.leavepolicy;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.util.List;

public record CreateLeavePolicyCommand(CompanyId companyId, String name, String countryCode, List<LeaveTypeDefInput> leaveTypes) {
    public record LeaveTypeDefInput(String leaveType, int annualEntitlementDays, int maxCarryoverDays, boolean requiresApproval, boolean requiresDocument, int minNoticeDays) {}
}
