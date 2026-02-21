package tr.kontas.erp.hr.application.payrollconfig;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.payrollconfig.PayrollConfig;

import java.util.List;

public interface GetPayrollConfigsByCompanyUseCase {
    List<PayrollConfig> execute(CompanyId companyId);
}
