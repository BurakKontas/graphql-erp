package tr.kontas.erp.hr.application.payrollrun;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.time.LocalDate;

public record CreatePayrollRunCommand(CompanyId companyId, int year, int month, LocalDate paymentDate, String payrollConfigId) {}
