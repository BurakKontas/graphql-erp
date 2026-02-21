package tr.kontas.erp.hr.application.payrollconfig;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;

public record CreatePayrollConfigCommand(CompanyId companyId, String countryCode, String name, int validYear, BigDecimal minimumWage, String currencyCode) {}
