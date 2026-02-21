package tr.kontas.erp.hr.application.payrollrun;

import tr.kontas.erp.hr.domain.payrollrun.PayrollRunId;

public interface CreatePayrollRunUseCase {
    PayrollRunId execute(CreatePayrollRunCommand command);
}
