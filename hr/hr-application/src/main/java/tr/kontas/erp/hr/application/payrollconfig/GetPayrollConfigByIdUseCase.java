package tr.kontas.erp.hr.application.payrollconfig;

import tr.kontas.erp.hr.domain.payrollconfig.PayrollConfig;
import tr.kontas.erp.hr.domain.payrollconfig.PayrollConfigId;

public interface GetPayrollConfigByIdUseCase {
    PayrollConfig execute(PayrollConfigId id);
}
