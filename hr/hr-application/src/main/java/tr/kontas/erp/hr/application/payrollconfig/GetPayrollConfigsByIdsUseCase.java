package tr.kontas.erp.hr.application.payrollconfig;

import tr.kontas.erp.hr.domain.payrollconfig.PayrollConfig;
import tr.kontas.erp.hr.domain.payrollconfig.PayrollConfigId;

import java.util.List;

public interface GetPayrollConfigsByIdsUseCase {
    List<PayrollConfig> execute(List<PayrollConfigId> ids);
}
