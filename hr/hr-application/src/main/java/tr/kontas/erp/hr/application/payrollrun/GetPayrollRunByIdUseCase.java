package tr.kontas.erp.hr.application.payrollrun;

import tr.kontas.erp.hr.domain.payrollrun.PayrollRun;
import tr.kontas.erp.hr.domain.payrollrun.PayrollRunId;

public interface GetPayrollRunByIdUseCase {
    PayrollRun execute(PayrollRunId id);
}
