package tr.kontas.erp.hr.application.payrollconfig;

import tr.kontas.erp.hr.domain.payrollconfig.PayrollConfigId;

public interface CreatePayrollConfigUseCase {
    PayrollConfigId execute(CreatePayrollConfigCommand command);
}
