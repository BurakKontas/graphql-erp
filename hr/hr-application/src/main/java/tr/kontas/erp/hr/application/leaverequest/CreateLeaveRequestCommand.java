package tr.kontas.erp.hr.application.leaverequest;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.time.LocalDate;

public record CreateLeaveRequestCommand(CompanyId companyId, String employeeId, String leaveType, LocalDate startDate, LocalDate endDate, int requestedDays, String reason, String documentRef) {}
