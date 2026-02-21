package tr.kontas.erp.hr.application.leavebalance;

import tr.kontas.erp.core.domain.company.CompanyId;

public record CreateLeaveBalanceCommand(CompanyId companyId, String employeeId, String leaveType, int year, int entitlementDays, int carryoverDays) {}
