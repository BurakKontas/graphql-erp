package tr.kontas.erp.hr.application.contract;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateContractCommand(CompanyId companyId, String employeeId, LocalDate startDate, LocalDate endDate, String contractType, BigDecimal grossSalary, String currencyCode, String payrollConfigId) {}
