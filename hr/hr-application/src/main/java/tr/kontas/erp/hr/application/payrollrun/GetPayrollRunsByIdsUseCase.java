package tr.kontas.erp.hr.application.payrollrun;

import tr.kontas.erp.hr.domain.payrollrun.PayrollRun;
import tr.kontas.erp.hr.domain.payrollrun.PayrollRunId;

import java.util.List;

public interface GetPayrollRunsByIdsUseCase {
    List<PayrollRun> execute(List<PayrollRunId> ids);
}
