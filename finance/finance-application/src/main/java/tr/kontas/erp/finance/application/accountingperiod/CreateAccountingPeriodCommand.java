package tr.kontas.erp.finance.application.accountingperiod;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.time.LocalDate;

public record CreateAccountingPeriodCommand(CompanyId companyId, String periodType, LocalDate startDate, LocalDate endDate) {}

