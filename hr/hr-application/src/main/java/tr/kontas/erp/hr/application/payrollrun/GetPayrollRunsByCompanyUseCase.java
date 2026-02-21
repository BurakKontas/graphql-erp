package tr.kontas.erp.hr.application.payrollrun;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.payrollrun.PayrollRun;

import java.util.List;

public interface GetPayrollRunsByCompanyUseCase {
    List<PayrollRun> execute(CompanyId companyId);
}
